package com.peer.dto;

import com.peer.entity.User;

public class AuthResponse {
    private String token;
    private String role;
    private Long id;
    private String fullName;
    private String email;
    private String section;
    private String department;
    private String userId;
    private String memberSince;

    public AuthResponse() {
    }

    public AuthResponse(String token, User user) {
        this.token = token;
        this.role = user.getRole().name();
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.section = user.getSection();
        this.department = user.getDepartment();
        this.userId = user.getUserId();
        this.memberSince = user.getCreatedAt() != null
                ? String.valueOf(user.getCreatedAt().getYear())
                : "Unknown";
    }

    public String getToken() { return token; }
    public String getRole() { return role; }
    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getSection() { return section; }
    public String getDepartment() { return department; }
    public String getUserId() { return userId; }
    public String getMemberSince() { return memberSince; }

    public void setToken(String token) { this.token = token; }
    public void setRole(String role) { this.role = role; }
    public void setId(Long id) { this.id = id; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setSection(String section) { this.section = section; }
    public void setDepartment(String department) { this.department = department; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setMemberSince(String memberSince) { this.memberSince = memberSince; }
}