package com.peer.dto;

import com.peer.entity.PeerReview;

import java.time.LocalDateTime;

public class PeerReviewResponse {
    private Long id;
    private Long submissionId;
    private String assignmentTitle;
    private String submissionText;
    private String fileName;
    private String filePath;
    private String peerId;
    private Integer score;
    private Integer maxScore;
    private String feedbackText;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private String dueDate;

    public PeerReviewResponse() {
    }

    public PeerReviewResponse(PeerReview pr) {
        this.id = pr.getId();
        this.submissionId = pr.getSubmission() != null ? pr.getSubmission().getId() : null;

        this.assignmentTitle = pr.getSubmission() != null && pr.getSubmission().getAssignment() != null
                ? pr.getSubmission().getAssignment().getTitle()
                : null;

        this.submissionText = pr.getSubmission() != null ? pr.getSubmission().getSubmissionText() : null;
        this.fileName = pr.getSubmission() != null ? pr.getSubmission().getFileName() : null;
        this.filePath = pr.getSubmission() != null ? pr.getSubmission().getFilePath() : null;

        this.peerId = pr.getSubmission() != null && pr.getSubmission().getStudent() != null
                ? "Anonymous Peer #" + (1000 + pr.getSubmission().getStudent().getId())
                : null;

        this.score = pr.getScore();
        this.maxScore = pr.getMaxScore();
        this.feedbackText = pr.getFeedbackText();
        this.status = pr.getStatus() != null ? pr.getStatus().name() : null;
        this.createdAt = pr.getCreatedAt();
        this.completedAt = pr.getCompletedAt();

        this.dueDate = pr.getSubmission() != null
                && pr.getSubmission().getAssignment() != null
                && pr.getSubmission().getAssignment().getDueDate() != null
                ? pr.getSubmission().getAssignment().getDueDate().toString()
                : null;
    }

    public Long getId() { return id; }
    public Long getSubmissionId() { return submissionId; }
    public String getAssignmentTitle() { return assignmentTitle; }
    public String getSubmissionText() { return submissionText; }
    public String getFileName() { return fileName; }
    public String getFilePath() { return filePath; }
    public String getPeerId() { return peerId; }
    public Integer getScore() { return score; }
    public Integer getMaxScore() { return maxScore; }
    public String getFeedbackText() { return feedbackText; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public String getDueDate() { return dueDate; }

    public void setId(Long id) { this.id = id; }
    public void setSubmissionId(Long submissionId) { this.submissionId = submissionId; }
    public void setAssignmentTitle(String assignmentTitle) { this.assignmentTitle = assignmentTitle; }
    public void setSubmissionText(String submissionText) { this.submissionText = submissionText; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public void setPeerId(String peerId) { this.peerId = peerId; }
    public void setScore(Integer score) { this.score = score; }
    public void setMaxScore(Integer maxScore) { this.maxScore = maxScore; }
    public void setFeedbackText(String feedbackText) { this.feedbackText = feedbackText; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
}