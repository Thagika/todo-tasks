package com.main.java.todo_app.service;

import com.main.java.todo_app.domain.Task;
import com.main.java.todo_app.domain.TaskId;
import com.main.java.todo_app.repository.TaskRepository;
import com.main.java.todo_app.validation.TaskValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DefaultTaskService implements TaskService {

    private final TaskRepository repository;
    private final TaskValidator validator;

    public DefaultTaskService(TaskRepository repository, TaskValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public Task createTask(String title, String description) {
        validator.ensureValidTitle(title);
        validator.ensureValidDescription(description);
        Task task = new Task(title, description);
        return repository.save(task);
    }

    @Override
    public Task completeTaskAndReturnNext(TaskId id) {
        Task task = repository.findById(id.getValue())
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        task.setCompleted(true);
        repository.save(task);

        // Return the next top uncompleted task
        List<Task> remaining = repository.findTop5ByCompletedFalseOrderByCreatedAtDesc();
        return remaining.isEmpty() ? null : remaining.get(0);
    }

    @Override
    public List<Task> getRecentTasks() {
        return repository.findTop5ByCompletedFalseOrderByCreatedAtDesc();
    }
}
