package com.peer.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assignments")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    private String section;
    private LocalDateTime dueDate;
    private Integer points = 100;
    private Integer peerReviewsRequired = 2;
    private boolean isGroup = false;
    private Integer groupLimit = 3;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public User getTeacher() { return teacher; }
    public String getSection() { return section; }
    public LocalDateTime getDueDate() { return dueDate; }
    public Integer getPoints() { return points; }
    public Integer getPeerReviewsRequired() { return peerReviewsRequired; }
    public boolean isGroup() { return isGroup; }
    public Integer getGroupLimit() { return groupLimit; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setTeacher(User teacher) { this.teacher = teacher; }
    public void setSection(String section) { this.section = section; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    public void setPoints(Integer points) { this.points = points; }
    public void setPeerReviewsRequired(Integer v) { this.peerReviewsRequired = v; }
    public void setGroup(boolean isGroup) { this.isGroup = isGroup; }
    public void setGroupLimit(Integer groupLimit) { this.groupLimit = groupLimit; }

    // Builder
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final Assignment a = new Assignment();
        public Builder title(String v) { a.title = v; return this; }
        public Builder description(String v) { a.description = v; return this; }
        public Builder teacher(User v) { a.teacher = v; return this; }
        public Builder section(String v) { a.section = v; return this; }
        public Builder dueDate(LocalDateTime v) { a.dueDate = v; return this; }
        public Builder points(Integer v) { a.points = v; return this; }
        public Builder peerReviewsRequired(Integer v) { a.peerReviewsRequired = v; return this; }
        public Builder isGroup(boolean v) { a.isGroup = v; return this; }
        public Builder groupLimit(Integer v) { a.groupLimit = v; return this; }
        public Assignment build() { return a; }
    }
}