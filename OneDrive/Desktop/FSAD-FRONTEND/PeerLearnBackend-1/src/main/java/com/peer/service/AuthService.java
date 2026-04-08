package com.peer.service;

import com.peer.dto.AuthResponse;
import com.peer.dto.LoginOtpSendRequest;
import com.peer.dto.LoginOtpVerifyRequest;
import com.peer.dto.LoginRequest;
import com.peer.dto.RegisterRequest;
import com.peer.entity.User;
import com.peer.repository.UserRepository;
import com.peer.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    private static final long OTP_TTL_MILLIS = 5 * 60 * 1000;
    private static final SecureRandom RANDOM = new SecureRandom();
    private final Map<String, OtpEntry> loginOtpStore = new ConcurrentHashMap<>();

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            AuthenticationManager authenticationManager,
            EmailService emailService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        if (request.getRole() == null) {
            throw new RuntimeException("Role is required");
        }

        String finalUserId;

        if (request.getRole() == User.Role.STUDENT) {
            String incomingStudentId = request.getStudentId() != null && !request.getStudentId().isBlank()
                    ? request.getStudentId().trim()
                    : (request.getUserId() != null ? request.getUserId().trim() : "");

            if (!incomingStudentId.matches("\\d{10}")) {
                throw new RuntimeException("Student ID must be exactly 10 digits");
            }
            finalUserId = incomingStudentId;

        } else if (request.getRole() == User.Role.TEACHER) {
            String incomingFacultyId = request.getFacultyId() != null && !request.getFacultyId().isBlank()
                    ? request.getFacultyId().trim()
                    : (request.getUserId() != null ? request.getUserId().trim() : "");

            if (!incomingFacultyId.matches("\\d{4}")) {
                throw new RuntimeException("Faculty ID must be exactly 4 digits");
            }
            finalUserId = incomingFacultyId;

        } else {
            throw new RuntimeException("Invalid role");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .section(request.getSection())
                .department(request.getDepartment())
                .userId(finalUserId)
                .active(true)
                .build();

        userRepository.save(user);
        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user);
    }

    public Map<String, String> sendLoginOtp(LoginOtpSendRequest request) {
        String normalizedEmail = request.getEmail() == null ? "" : request.getEmail().trim().toLowerCase();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            log.info("OTP login auth success for {}", normalizedEmail);
        } catch (BadCredentialsException e) {
            log.warn("OTP login failed: bad credentials for {}", normalizedEmail);
            throw new RuntimeException("Invalid email or password");
        } catch (Exception e) {
            log.error("OTP login failed: auth error for {}", normalizedEmail, e);
            throw new RuntimeException("Authentication failed");
        }

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> {
                    log.warn("OTP login failed: user not found for {}", normalizedEmail);
                    return new RuntimeException("User not found");
                });

        String requestRole = request.getRole() == null ? "" : request.getRole().trim();
        if (!user.getRole().name().equalsIgnoreCase(requestRole)) {
            log.warn("OTP login failed: role mismatch for {}, expected={}, got={}",
                    normalizedEmail, user.getRole().name(), requestRole);
            throw new RuntimeException("Please select the correct role");
        }

        cleanupExpiredOtps();

        String otp = String.format("%06d", RANDOM.nextInt(1_000_000));
        long expiresAt = Instant.now().toEpochMilli() + OTP_TTL_MILLIS;
        loginOtpStore.put(normalizedEmail, new OtpEntry(otp, expiresAt));

        try {
            emailService.sendLoginOtpEmail(user.getEmail(), user.getFullName(), otp);
            log.info("OTP email sent to {}", normalizedEmail);
        } catch (Exception e) {
            log.error("OTP email send failed for {}", normalizedEmail, e);
            throw new RuntimeException("Failed to send OTP email");
        }

        return Map.of("message", "OTP sent to your email");
    }

    public AuthResponse verifyLoginOtp(LoginOtpVerifyRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String normalizedEmail = request.getEmail().trim().toLowerCase();
        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getRole().name().equalsIgnoreCase(request.getRole())) {
            throw new RuntimeException("Please select the correct role");
        }

        OtpEntry entry = loginOtpStore.get(normalizedEmail);
        if (entry == null) {
            throw new RuntimeException("OTP not found. Please request a new OTP");
        }

        if (Instant.now().toEpochMilli() > entry.expiresAt()) {
            loginOtpStore.remove(normalizedEmail);
            throw new RuntimeException("OTP expired. Please request a new OTP");
        }

        if (!entry.code().equals(request.getOtp().trim())) {
            throw new RuntimeException("Invalid OTP");
        }

        loginOtpStore.remove(normalizedEmail);

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user);
    }

    private void cleanupExpiredOtps() {
        long now = Instant.now().toEpochMilli();
        loginOtpStore.entrySet().removeIf(e -> e.getValue().expiresAt() < now);
    }

    private record OtpEntry(String code, long expiresAt) {}
}