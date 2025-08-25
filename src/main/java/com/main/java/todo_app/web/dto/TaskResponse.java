package com.main.java.todo_app.web.dto;

import com.main.java.todo_app.domain.Task;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.text.StringEscapeUtils;

import java.time.LocalDateTime;

@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.ANY)
public class TaskResponse {

    private final String id;
    private final String title;          // raw value
    private final String description;    // raw value
    private final boolean completed;
    private final LocalDateTime createdAt;

    public TaskResponse(Task task) {
        this.id = task.getId().toString();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.completed = task.isCompleted();
        this.createdAt = task.getCreatedAt();
    }

    // ---------- Raw values ----------
    @JsonProperty("id")
    public String getId() { return id; }

    @JsonProperty("title")
    public String getTitle() { return title; }

    @JsonProperty("description")
    public String getDescription() { return description; }

    @JsonProperty("completed")
    public boolean isCompleted() { return completed; }

    @JsonProperty("createdAt")
    public LocalDateTime getCreatedAt() { return createdAt; }

    // ---------- Escaped values for front-end ----------
    @JsonProperty("titleForHtml")
    public String getTitleForHtml() {
        return title != null ? StringEscapeUtils.escapeHtml4(title) : null;
    }

    @JsonProperty("descriptionForHtml")
    public String getDescriptionForHtml() {
        return description != null ? StringEscapeUtils.escapeHtml4(description) : null;
    }
}
