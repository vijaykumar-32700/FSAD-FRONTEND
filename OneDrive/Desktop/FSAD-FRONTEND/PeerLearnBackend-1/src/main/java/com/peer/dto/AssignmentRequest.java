package com.peer.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.time.LocalDateTime;

public class AssignmentRequest {

    private String title;
    private String description;
    private String section;
    private LocalDateTime dueDate;
    private Integer points = 100;
    private Integer peerReviewsRequired = 2;

    @JsonAlias({"isGroupProject", "groupProject"})
    private boolean isGroup = false;

    private Integer groupLimit = 3;

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getSection() { return section; }
    public LocalDateTime getDueDate() { return dueDate; }
    public Integer getPoints() { return points; }
    public Integer getPeerReviewsRequired() { return peerReviewsRequired; }
    public boolean isGroup() { return isGroup; }
    public Integer getGroupLimit() { return groupLimit; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setSection(String section) { this.section = section; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    public void setPoints(Integer points) { this.points = points; }
    public void setPeerReviewsRequired(Integer peerReviewsRequired) { this.peerReviewsRequired = peerReviewsRequired; }
    public void setGroup(boolean group) { isGroup = group; }
    public void setGroupLimit(Integer groupLimit) { this.groupLimit = groupLimit; }
}