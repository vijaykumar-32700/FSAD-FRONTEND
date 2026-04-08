package com.peer.dto;

import com.peer.entity.User;

public class RegisterRequest {

    private String fullName;
    private String email;
    private String password;
    private User.Role role;
    private String section;
    private String department;

    // NEW: ID fields coming from frontend signup
    private String userId;
    private String studentId;
    private String facultyId;

    // Getters
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public User.Role getRole() { return role; }
    public String getSection() { return section; }
    public String getDepartment() { return department; }
    public String getUserId() { return userId; }
    public String getStudentId() { return studentId; }
    public String getFacultyId() { return facultyId; }

    // Setters
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(User.Role role) { this.role = role; }
    public void setSection(String section) { this.section = section; }
    public void setDepartment(String department) { this.department = department; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public void setFacultyId(String facultyId) { this.facultyId = facultyId; }
}