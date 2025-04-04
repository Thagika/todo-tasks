package com.main.java.todo_app;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import java.sql.Timestamp;
import jakarta.persistence.PrePersist;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Column(name = "is_completed") // Ensure this matches your database column name
    private int isCompleted; // Use camelCase

    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    // Constructors
    public Task() {}

    public Task(String title, String description, int isCompleted) {
        this.title = title;
        this.description = description;
        this.isCompleted = isCompleted;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(int isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    @PrePersist
    private void onCreate() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }
}
