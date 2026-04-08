package com.peer.dto;

import com.peer.entity.Submission;

import java.nio.file.Paths;
import java.time.LocalDateTime;

public class SubmissionResponse {
    private Long id;
    private Long assignmentId;
    private String assignmentTitle;
    private Long studentId;
    private String studentName;
    private String studentUserId;
    private String submissionText;
    private String fileName;
    private String filePath;
    private String status;
    private Integer teacherScore;
    private String teacherFeedback;
    private LocalDateTime submittedAt;
    private LocalDateTime gradedAt;
    private Integer maxScore;
    private Double averagePeerScore;
    private Integer peerReviewCount;

    public SubmissionResponse() {
    }

    public SubmissionResponse(Submission s) {
        this.id = s.getId();
        this.assignmentId = s.getAssignment() != null ? s.getAssignment().getId() : null;
        this.assignmentTitle = s.getAssignment() != null ? s.getAssignment().getTitle() : null;
        this.studentId = s.getStudent() != null ? s.getStudent().getId() : null;
        this.studentName = s.getStudent() != null ? s.getStudent().getFullName() : null;
        this.studentUserId = s.getStudent() != null ? s.getStudent().getUserId() : null;
        this.submissionText = s.getSubmissionText();
        this.fileName = s.getFileName();
        this.filePath = normalizeFilePath(s.getFilePath(), s.getFileName());
        this.status = s.getStatus() != null ? s.getStatus().name() : null;
        this.teacherScore = s.getTeacherScore();
        this.teacherFeedback = s.getTeacherFeedback();
        this.submittedAt = s.getSubmittedAt();
        this.gradedAt = s.getGradedAt();
        this.maxScore = s.getAssignment() != null ? s.getAssignment().getPoints() : null;
        this.averagePeerScore = s.getAveragePeerScore() != null ? s.getAveragePeerScore() : 0.0;
        this.peerReviewCount = s.getPeerReviewCount() != null ? s.getPeerReviewCount() : 0;
    }

    private String normalizeFilePath(String rawPath, String fileName) {
        if (rawPath == null || rawPath.isBlank()) {
            return null;
        }

        String normalized = rawPath.trim().replace("\\", "/");

        if (normalized.startsWith("http://") || normalized.startsWith("https://") || normalized.startsWith("/uploads/")) {
            return normalized;
        }

        String resolvedName = fileName;
        if (resolvedName == null || resolvedName.isBlank()) {
            try {
                resolvedName = Paths.get(normalized).getFileName().toString();
            } catch (Exception ex) {
                resolvedName = normalized;
            }
        }

        return "/uploads/" + resolvedName;
    }

    public Long getId() { return id; }
    public Long getAssignmentId() { return assignmentId; }
    public String getAssignmentTitle() { return assignmentTitle; }
    public Long getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public String getStudentUserId() { return studentUserId; }
    public String getSubmissionText() { return submissionText; }
    public String getFileName() { return fileName; }
    public String getFilePath() { return filePath; }
    public String getStatus() { return status; }
    public Integer getTeacherScore() { return teacherScore; }
    public String getTeacherFeedback() { return teacherFeedback; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public LocalDateTime getGradedAt() { return gradedAt; }
    public Integer getMaxScore() { return maxScore; }
    public Double getAveragePeerScore() { return averagePeerScore; }
    public Integer getPeerReviewCount() { return peerReviewCount; }

    public void setId(Long id) { this.id = id; }
    public void setAssignmentId(Long assignmentId) { this.assignmentId = assignmentId; }
    public void setAssignmentTitle(String assignmentTitle) { this.assignmentTitle = assignmentTitle; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public void setStudentUserId(String studentUserId) { this.studentUserId = studentUserId; }
    public void setSubmissionText(String submissionText) { this.submissionText = submissionText; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public void setStatus(String status) { this.status = status; }
    public void setTeacherScore(Integer teacherScore) { this.teacherScore = teacherScore; }
    public void setTeacherFeedback(String teacherFeedback) { this.teacherFeedback = teacherFeedback; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
    public void setGradedAt(LocalDateTime gradedAt) { this.gradedAt = gradedAt; }
    public void setMaxScore(Integer maxScore) { this.maxScore = maxScore; }
    public void setAveragePeerScore(Double averagePeerScore) { this.averagePeerScore = averagePeerScore; }
    public void setPeerReviewCount(Integer peerReviewCount) { this.peerReviewCount = peerReviewCount; }
}