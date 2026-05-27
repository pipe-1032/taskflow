package com.taskflow.taskflow.dto;

public class NotificationRequest {

    private String message;
    private String type;
    private Long userId;

    public NotificationRequest() {
    }

    public NotificationRequest(String message, String type, Long userId) {
        this.message = message;
        this.type = type;
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}