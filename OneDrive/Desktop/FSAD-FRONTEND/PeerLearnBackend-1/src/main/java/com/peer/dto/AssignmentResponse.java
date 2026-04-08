package com.peer.dto;

import com.peer.entity.Assignment;

import java.time.LocalDateTime;

public class AssignmentResponse {
    private Long id;
    private String title;
    private String description;
    private String teacherName;
    private Long teacherId;
    private String section;
    private LocalDateTime dueDate;
    private Integer points;
    private Integer peerReviewsRequired;
    private boolean isGroup;
    private Integer groupLimit;
    private LocalDateTime createdAt;
    private int submissionCount;

    public AssignmentResponse() {
    }

    public AssignmentResponse(Assignment a) {
        this(a, 0);
    }

    public AssignmentResponse(Assignment a, int submissionCount) {
        this.id = a.getId();
        this.title = a.getTitle();
        this.description = a.getDescription();
        if (a.getTeacher() != null) {
            this.teacherName = a.getTeacher().getFullName();
            this.teacherId = a.getTeacher().getId();
        }
        this.section = a.getSection();
        this.dueDate = a.getDueDate();
        this.points = a.getPoints();
        this.peerReviewsRequired = a.getPeerReviewsRequired();
        this.isGroup = a.isGroup();
        this.groupLimit = a.getGroupLimit();
        this.createdAt = a.getCreatedAt();
        this.submissionCount = submissionCount;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getTeacherName() { return teacherName; }
    public Long getTeacherId() { return teacherId; }
    public String getSection() { return section; }
    public LocalDateTime getDueDate() { return dueDate; }
    public Integer getPoints() { return points; }
    public Integer getPeerReviewsRequired() { return peerReviewsRequired; }
    public boolean isGroup() { return isGroup; }
    public Integer getGroupLimit() { return groupLimit; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public int getSubmissionCount() { return submissionCount; }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }
    public void setSection(String section) { this.section = section; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    public void setPoints(Integer points) { this.points = points; }
    public void setPeerReviewsRequired(Integer peerReviewsRequired) { this.peerReviewsRequired = peerReviewsRequired; }
    public void setGroup(boolean group) { isGroup = group; }
    public void setGroupLimit(Integer groupLimit) { this.groupLimit = groupLimit; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setSubmissionCount(int submissionCount) { this.submissionCount = submissionCount; }
}