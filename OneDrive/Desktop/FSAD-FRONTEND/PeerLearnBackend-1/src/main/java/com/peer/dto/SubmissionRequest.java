package com.peer.dto;

public class SubmissionRequest {

    private Long assignmentId;
    private String submissionText;

    // Getters
    public Long getAssignmentId() { return assignmentId; }
    public String getSubmissionText() { return submissionText; }

    // Setters
    public void setAssignmentId(Long assignmentId) { this.assignmentId = assignmentId; }
    public void setSubmissionText(String submissionText) { this.submissionText = submissionText; }
}