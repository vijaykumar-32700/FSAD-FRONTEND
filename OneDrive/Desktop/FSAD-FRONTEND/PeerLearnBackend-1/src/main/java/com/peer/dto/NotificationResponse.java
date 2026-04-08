package com.peer.dto;

import com.peer.entity.Notification;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class NotificationResponse {
    private Long id;
    private String title;
    private String message;
    private String type;
    private boolean read;
    private String time;
    private LocalDateTime createdAt;

    public NotificationResponse() {
    }

    public NotificationResponse(Notification n) {
        this.id = n.getId();
        this.title = n.getTitle();
        this.message = n.getMessage();
        this.type = n.getType() != null ? n.getType().name().toLowerCase() : null;
        this.read = n.isRead();
        this.createdAt = n.getCreatedAt();
        this.time = formatTime(n.getCreatedAt());
    }

    private String formatTime(LocalDateTime time) {
        if (time == null) return "Just now";
        long minutes = ChronoUnit.MINUTES.between(time, LocalDateTime.now());
        if (minutes < 60) return minutes + " minutes ago";
        long hours = ChronoUnit.HOURS.between(time, LocalDateTime.now());
        if (hours < 24) return hours + " hours ago";
        long days = ChronoUnit.DAYS.between(time, LocalDateTime.now());
        return days + " days ago";
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public String getType() { return type; }
    public boolean isRead() { return read; }
    public String getTime() { return time; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setMessage(String message) { this.message = message; }
    public void setType(String type) { this.type = type; }
    public void setRead(boolean read) { this.read = read; }
    public void setTime(String time) { this.time = time; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}