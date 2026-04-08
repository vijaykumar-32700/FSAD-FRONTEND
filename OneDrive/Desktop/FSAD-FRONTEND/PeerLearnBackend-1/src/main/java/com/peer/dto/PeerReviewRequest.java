package com.peer.dto;

public class PeerReviewRequest {

    private Long submissionId;
    private Integer score;
    private String feedbackText;

    // Getters
    public Long getSubmissionId() { return submissionId; }
    public Integer getScore() { return score; }
    public String getFeedbackText() { return feedbackText; }

    // Setters
    public void setSubmissionId(Long submissionId) { this.submissionId = submissionId; }
    public void setScore(Integer score) { this.score = score; }
    public void setFeedbackText(String feedbackText) { this.feedbackText = feedbackText; }
}