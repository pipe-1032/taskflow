package com.taskflow.taskflow.dto;

public class NotificationResponse {

    private Long id;
    private String message;
    private boolean read;
    private String type;
    private String createdAt;
    private UserResponse user;

    public NotificationResponse() {
    }

    public NotificationResponse(Long id, String message, boolean read, String type, String createdAt, UserResponse user) {
        this.id = id;
        this.message = message;
        this.read = read;
        this.type = type;
        this.createdAt = createdAt;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public boolean isRead() {
        return read;
    }

    public String getType() {
        return type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }
}