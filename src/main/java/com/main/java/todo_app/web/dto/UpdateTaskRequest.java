package com.main.java.todo_app.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateTaskRequest {
    @NotBlank
    @Size(max = 200)
    private String title;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}
