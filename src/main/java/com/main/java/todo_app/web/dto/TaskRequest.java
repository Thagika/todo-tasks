package com.main.java.todo_app.web.dto;

public class TaskRequest {
    private String title;
    private String description;
    private Boolean completed;

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Boolean getCompleted() { return completed; }
}
