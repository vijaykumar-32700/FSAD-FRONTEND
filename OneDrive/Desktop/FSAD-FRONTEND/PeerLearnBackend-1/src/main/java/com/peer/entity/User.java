package com.peer.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String section;
    private String userId;
    private String department;

    @Column(nullable = false)
    private boolean active = true;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum Role {
        STUDENT, TEACHER
    }

    // Getters
    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
    public String getSection() { return section; }
    public String getUserId() { return userId; }
    public String getDepartment() { return department; }
    public boolean isActive() { return active; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(Role role) { this.role = role; }
    public void setSection(String section) { this.section = section; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setDepartment(String department) { this.department = department; }
    public void setActive(boolean active) { this.active = active; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final User u = new User();
        public Builder fullName(String v) { u.fullName = v; return this; }
        public Builder email(String v) { u.email = v; return this; }
        public Builder password(String v) { u.password = v; return this; }
        public Builder role(Role v) { u.role = v; return this; }
        public Builder section(String v) { u.section = v; return this; }
        public Builder userId(String v) { u.userId = v; return this; }
        public Builder department(String v) { u.department = v; return this; }
        public Builder active(boolean v) { u.active = v; return this; }
        public User build() { return u; }
    }
}