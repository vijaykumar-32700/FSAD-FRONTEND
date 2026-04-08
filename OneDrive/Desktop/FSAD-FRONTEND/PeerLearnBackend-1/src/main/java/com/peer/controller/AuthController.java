package com.peer.controller;

import com.peer.dto.AuthResponse;
import com.peer.dto.LoginOtpSendRequest;
import com.peer.dto.LoginOtpVerifyRequest;
import com.peer.dto.LoginRequest;
import com.peer.dto.RegisterRequest;
import com.peer.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Map.of(
                            "timestamp", Instant.now().toString(),
                            "status", 400,
                            "error", "Bad Request",
                            "message", e.getMessage()
                    )
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of(
                            "timestamp", Instant.now().toString(),
                            "status", 401,
                            "error", "Unauthorized",
                            "message", "Invalid email or password"
                    )
            );
        }
    }

    @PostMapping("/login/send-otp")
    public ResponseEntity<?> sendLoginOtp(@Valid @RequestBody LoginOtpSendRequest request) {
        try {
            return ResponseEntity.ok(authService.sendLoginOtp(request));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Map.of(
                            "timestamp", Instant.now().toString(),
                            "status", 400,
                            "error", "Bad Request",
                            "message", e.getMessage()
                    )
            );
        }
    }

    @PostMapping("/login/verify-otp")
    public ResponseEntity<?> verifyLoginOtp(@Valid @RequestBody LoginOtpVerifyRequest request) {
        try {
            AuthResponse response = authService.verifyLoginOtp(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of(
                            "timestamp", Instant.now().toString(),
                            "status", 401,
                            "error", "Unauthorized",
                            "message", e.getMessage()
                    )
            );
        }
    }
}