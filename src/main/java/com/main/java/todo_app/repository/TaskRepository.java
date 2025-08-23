package com.main.java.todo_app.repository;

import com.main.java.todo_app.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Fetch top 5 uncompleted tasks sorted by creation time descending
    List<Task> findTop5ByCompletedFalseOrderByCreatedAtDesc();
}
