package com.peer.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "group_members")
public class GroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Column(updatable = false)
    private LocalDateTime joinedAt;

    @PrePersist
    protected void onCreate() { joinedAt = LocalDateTime.now(); }

    // Getters
    public Long getId() { return id; }
    public Assignment getAssignment() { return assignment; }
    public User getStudent() { return student; }
    public LocalDateTime getJoinedAt() { return joinedAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setAssignment(Assignment a) { this.assignment = a; }
    public void setStudent(User s) { this.student = s; }

    // Builder
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final GroupMember gm = new GroupMember();
        public Builder assignment(Assignment v) { gm.assignment = v; return this; }
        public Builder student(User v) { gm.student = v; return this; }
        public GroupMember build() { return gm; }
    }
}