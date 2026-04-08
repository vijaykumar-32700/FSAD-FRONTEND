package com.peer.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resources")
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String type;
    private String format;
    private String link;
    private Integer downloadCount = 0;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    // Getters
    public Long getId() { return id; }
    public User getTeacher() { return teacher; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getType() { return type; }
    public String getFormat() { return format; }
    public String getLink() { return link; }
    public Integer getDownloadCount() { return downloadCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setTeacher(User t) { this.teacher = t; }
    public void setTitle(String t) { this.title = t; }
    public void setDescription(String d) { this.description = d; }
    public void setType(String t) { this.type = t; }
    public void setFormat(String f) { this.format = f; }
    public void setLink(String l) { this.link = l; }
    public void setDownloadCount(Integer d) { this.downloadCount = d; }

    // Builder
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final Resource r = new Resource();
        public Builder teacher(User v) { r.teacher = v; return this; }
        public Builder title(String v) { r.title = v; return this; }
        public Builder description(String v) { r.description = v; return this; }
        public Builder type(String v) { r.type = v; return this; }
        public Builder format(String v) { r.format = v; return this; }
        public Builder link(String v) { r.link = v; return this; }
        public Builder downloadCount(Integer v) { r.downloadCount = v; return this; }
        public Resource build() { return r; }
    }
}