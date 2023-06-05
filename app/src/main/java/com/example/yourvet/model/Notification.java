package com.example.yourvet.model;

import java.util.UUID;

public class Notification {
    private String id;
    private String title;
    private String message;
    private String userId;
    private String  time;
    private String interventionId;
    private boolean seen;
    public Notification(String title, String message, String userId, String  time) {
        this.id= UUID.randomUUID().toString();
        this.title = title;
        this.message = message;
        this.userId = userId;
        this.time = time;
        this.seen =false;
    }

    public Notification(String title, String message, String userId, String time, String interventionId) {
        this.id=UUID.randomUUID().toString();
        this.id = id;
        this.title = title;
        this.message = message;
        this.userId = userId;
        this.time = time;
        this.interventionId = interventionId;
        this.seen = false;
    }

    public Notification() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getInterventionId() {
        return interventionId;
    }

    public void setInterventionId(String interventionId) {
        this.interventionId = interventionId;
    }
}
