package com.peer.service;

import com.peer.entity.User;
import com.peer.repository.PeerReviewRepository;
import com.peer.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PeerReviewRepository peerReviewRepository;

    public UserService(UserRepository userRepository, PeerReviewRepository peerReviewRepository) {
        this.userRepository = userRepository;
        this.peerReviewRepository = peerReviewRepository;
    }

    public Map<String, Object> getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return buildProfileMap(user);
    }

    public Map<String, Object> getStudentById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return buildProfileMap(user);
    }

    public List<Map<String, Object>> getStudents(String section) {
        List<User> users;
        if (section != null && !section.isEmpty()) {
            users = userRepository.findByRoleAndSection(User.Role.STUDENT, section);
        } else {
            users = userRepository.findByRole(User.Role.STUDENT);
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (User user : users) {
            result.add(buildProfileMap(user));
        }
        return result;
    }

    public List<Map<String, Object>> getAllStudents() {
        List<User> users = userRepository.findByRole(User.Role.STUDENT);
        List<Map<String, Object>> result = new ArrayList<>();
        for (User user : users) {
            result.add(buildProfileMap(user));
        }
        return result;
    }

    private Map<String, Object> buildProfileMap(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("fullName", user.getFullName());
        map.put("email", user.getEmail());
        map.put("role", user.getRole().name());
        map.put("section", user.getSection());
        map.put("department", user.getDepartment());
        map.put("userId", user.getUserId());
        map.put("active", user.isActive());
        map.put("memberSince",
                user.getCreatedAt() != null
                        ? String.valueOf(user.getCreatedAt().getYear())
                        : "Unknown");

        Double averagePeerScore = 0.0;
        if (user.getRole() == User.Role.STUDENT) {
            Double avg = peerReviewRepository.findAveragePeerScoreByStudent(user);
            averagePeerScore = avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;
        }
        map.put("averagePeerScore", averagePeerScore);

        String fullName = user.getFullName();
        if (fullName != null && !fullName.isEmpty()) {
            String[] parts = fullName.trim().split("\\s+");
            StringBuilder initials = new StringBuilder();
            for (String part : parts) {
                if (!part.isEmpty()) {
                    initials.append(Character.toUpperCase(part.charAt(0)));
                }
            }
            map.put("initials", initials.toString());
        } else {
            map.put("initials", "?");
        }

        return map;
    }
}