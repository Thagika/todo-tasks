package com.main.java.todo_app;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    // Additional query methods can be defined here if needed
}
