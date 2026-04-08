package com.peer.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Column(columnDefinition = "TEXT")
    private String submissionText;

    private String filePath;
    private String fileName;
    
   

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING_REVIEW;

    private Integer teacherScore;

    @Column(columnDefinition = "TEXT")
    private String teacherFeedback;

    @Column(updatable = false)
    private LocalDateTime submittedAt;

    private LocalDateTime gradedAt;
    private Double averagePeerScore;
    private Integer peerReviewCount = 0;

    @PrePersist
    protected void onCreate() { submittedAt = LocalDateTime.now(); }

    public enum Status { PENDING_REVIEW, PEER_REVIEWED, FULLY_GRADED }

    // Getters
    public Long getId() { return id; }
    public Assignment getAssignment() { return assignment; }
    public User getStudent() { return student; }
    public String getSubmissionText() { return submissionText; }
    public String getFilePath() { return filePath; }
    public String getFileName() { return fileName; }
    public Status getStatus() { return status; }
    public Integer getTeacherScore() { return teacherScore; }
    public String getTeacherFeedback() { return teacherFeedback; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public LocalDateTime getGradedAt() { return gradedAt; }
    public Double getAveragePeerScore() { return averagePeerScore; }
    public Integer getPeerReviewCount() { return peerReviewCount; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setAssignment(Assignment a) { this.assignment = a; }
    public void setStudent(User s) { this.student = s; }
    public void setSubmissionText(String t) { this.submissionText = t; }
    public void setFilePath(String f) { this.filePath = f; }
    public void setFileName(String f) { this.fileName = f; }
    public void setStatus(Status s) { this.status = s; }
    public void setTeacherScore(Integer s) { this.teacherScore = s; }
    public void setTeacherFeedback(String f) { this.teacherFeedback = f; }
    public void setSubmittedAt(LocalDateTime t) { this.submittedAt = t; }
    public void setGradedAt(LocalDateTime t) { this.gradedAt = t; }
    public void setAveragePeerScore(Double score) { this.averagePeerScore = score; }
    public void setPeerReviewCount(Integer count) { this.peerReviewCount = count; }

    // Builder
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final Submission s = new Submission();
        public Builder assignment(Assignment v) { s.assignment = v; return this; }
        public Builder student(User v) { s.student = v; return this; }
        public Builder submissionText(String v) { s.submissionText = v; return this; }
        public Builder fileName(String v) { s.fileName = v; return this; }
        public Builder filePath(String v) { s.filePath = v; return this; }
        public Builder status(Status v) { s.status = v; return this; }
        public Submission build() { return s; }
        public Builder averagePeerScore(Double v) { s.averagePeerScore = v; return this; }
        public Builder peerReviewCount(Integer v) { s.peerReviewCount = v; return this; }
    }
}