package com.main.java.todo_app.web.dto;

import com.main.java.todo_app.domain.Task;
import com.main.java.todo_app.domain.TaskId;

import java.time.LocalDateTime;

public class TaskResponse {

    private TaskId id;
    private String title;
    private String description;
    private boolean completed;
    private LocalDateTime createdAt;

    public TaskResponse(Task task) {
        this.id = TaskId.fromLong(task.getId()); // fixed mapping
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.completed = task.isCompleted();
        this.createdAt = task.getCreatedAt();
    }

    public TaskId getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public boolean isCompleted() { return completed; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
