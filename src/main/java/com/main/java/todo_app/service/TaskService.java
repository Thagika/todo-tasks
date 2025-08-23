package com.main.java.todo_app.service;

import com.main.java.todo_app.domain.Task;
import com.main.java.todo_app.domain.TaskId;

import java.util.List;

public interface TaskService {
    Task createTask(String title, String description);
    Task completeTaskAndReturnNext(TaskId id); // returns next available task
    List<Task> getRecentTasks(); // top 5 uncompleted tasks
}
