package com.peer.dto;

import com.peer.entity.Resource;

import java.time.LocalDateTime;

public class ResourceResponse {
    private Long id;
    private String title;
    private String description;
    private String type;
    private String format;
    private String link;
    private Integer downloadCount;
    private String teacherName;
    private LocalDateTime createdAt;

    public ResourceResponse() {
    }

    public ResourceResponse(Resource r) {
        this.id = r.getId();
        this.title = r.getTitle();
        this.description = r.getDescription();
        this.type = r.getType();
        this.format = r.getFormat();
        this.link = r.getLink();
        this.downloadCount = r.getDownloadCount();
        this.teacherName = r.getTeacher() != null ? r.getTeacher().getFullName() : null;
        this.createdAt = r.getCreatedAt();
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getType() { return type; }
    public String getFormat() { return format; }
    public String getLink() { return link; }
    public Integer getDownloadCount() { return downloadCount; }
    public String getTeacherName() { return teacherName; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setType(String type) { this.type = type; }
    public void setFormat(String format) { this.format = format; }
    public void setLink(String link) { this.link = link; }
    public void setDownloadCount(Integer downloadCount) { this.downloadCount = downloadCount; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}