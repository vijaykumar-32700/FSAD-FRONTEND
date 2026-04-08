package com.peer.dto;

public class ResourceRequest {

    private String title;
    private String description;
    private String type;
    private String format;
    private String link;

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getType() { return type; }
    public String getFormat() { return format; }
    public String getLink() { return link; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setType(String type) { this.type = type; }
    public void setFormat(String format) { this.format = format; }
    public void setLink(String link) { this.link = link; }
}