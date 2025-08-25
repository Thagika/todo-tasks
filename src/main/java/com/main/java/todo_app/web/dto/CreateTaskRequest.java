package com.main.java.todo_app.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateTaskRequest {
    @NotBlank
    @Size(max = 200)
    private String title;

    @Size(max = 1000)
    private String description;

    public String getTitle() { return title; }

    public void setTitle(String title) {
        // Store raw value, escaping is handled later in TaskResponse
        this.title = title;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) {
        // Store raw value, escaping is handled later in TaskResponse
        this.description = description;
    }
}
