package com.main.java.todo_app.controller;

import com.main.java.todo_app.domain.Task;
import com.main.java.todo_app.domain.TaskId;
import com.main.java.todo_app.service.TaskService;
import com.main.java.todo_app.web.dto.CreateTaskRequest;
import com.main.java.todo_app.web.dto.TaskResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest request) {
        Task task = taskService.createTask(request.getTitle(), request.getDescription());
        return ResponseEntity.ok(new TaskResponse(task));
    }



    @PostMapping("/{id}/complete")
    public ResponseEntity<TaskResponse> completeTask(@PathVariable("id") String id) {
        if (!id.matches("\\d+")) {
            return ResponseEntity.badRequest().build();
        }

        Task nextTask = taskService.completeTaskAndReturnNext(TaskId.fromString(id));
        return ResponseEntity.ok(nextTask != null ? new TaskResponse(nextTask) : null);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<TaskResponse>> getRecentTasks() {
        List<TaskResponse> recentTasks = taskService.getRecentTasks()
                .stream()
                .map(TaskResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(recentTasks);
    }
}
