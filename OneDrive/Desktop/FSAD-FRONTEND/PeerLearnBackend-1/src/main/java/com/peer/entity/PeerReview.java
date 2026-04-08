package com.peer.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "peer_reviews")
public class PeerReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false)
    private Submission submission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    private Integer score;
    private Integer maxScore;

    @Column(columnDefinition = "TEXT")
    private String feedbackText;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime completedAt;
    // NEW FIELDS FOR AUTO-GENERATION
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    private LocalDateTime dueDate;
    private Long submittedByStudentId;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    public enum Status { PENDING, COMPLETED }

    // Getters
    public Long getId() { return id; }
    public Submission getSubmission() { return submission; }
    public User getReviewer() { return reviewer; }
    public Integer getScore() { return score; }
    public Integer getMaxScore() { return maxScore; }
    public String getFeedbackText() { return feedbackText; }
    public Status getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public Assignment getAssignment() { return assignment; }
    public LocalDateTime getDueDate() { return dueDate; }
    public Long getSubmittedByStudentId() { return submittedByStudentId; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setSubmission(Submission s) { this.submission = s; }
    public void setReviewer(User r) { this.reviewer = r; }
    public void setScore(Integer s) { this.score = s; }
    public void setMaxScore(Integer m) { this.maxScore = m; }
    public void setFeedbackText(String f) { this.feedbackText = f; }
    public void setStatus(Status s) { this.status = s; }
    public void setCompletedAt(LocalDateTime t) { this.completedAt = t; }
    public void setAssignment(Assignment a) { this.assignment = a; }
    public void setDueDate(LocalDateTime d) { this.dueDate = d; }
    public void setSubmittedByStudentId(Long id) { this.submittedByStudentId = id; }

    // Builder
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final PeerReview pr = new PeerReview();
        public Builder submission(Submission v) { pr.submission = v; return this; }
        public Builder reviewer(User v) { pr.reviewer = v; return this; }
        public Builder maxScore(Integer v) { pr.maxScore = v; return this; }
        public Builder status(Status v) { pr.status = v; return this; }
        public PeerReview build() { return pr; }
        public Builder assignment(Assignment v) { pr.assignment = v; return this; }
        public Builder dueDate(LocalDateTime v) { pr.dueDate = v; return this; }
        public Builder submittedByStudentId(Long v) { pr.submittedByStudentId = v; return this; }
    }
}