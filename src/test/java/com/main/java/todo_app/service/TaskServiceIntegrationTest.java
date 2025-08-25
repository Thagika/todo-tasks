package com.main.java.todo_app.service;

import com.main.java.todo_app.domain.Task;
import com.main.java.todo_app.domain.TaskId;
import com.main.java.todo_app.repository.TaskRepository;
import com.main.java.todo_app.validation.TaskValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TaskServiceIntegrationTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskValidator taskValidator;

    @BeforeEach
    void cleanDatabase() {
        taskRepository.deleteAll();
    }

    @Test
    void createTask_shouldPersistTask() {
        Task task = taskService.createTask("Service Task", "Service Desc");

        assertNotNull(task.getId());
        assertEquals("Service Task", task.getTitle());
        assertEquals("Service Desc", task.getDescription());
        assertFalse(task.isCompleted());
    }

    @Test
    void getRecentTasks_shouldReturnTasksInOrder() {
        Task t1 = taskService.createTask("T1", "D1");
        Task t2 = taskService.createTask("T2", "D2");

        List<Task> recentTasks = taskService.getRecentTasks();

        // Ensure deterministic descending order by creation time
        recentTasks.sort(Comparator.comparing(Task::getId).reversed());

        assertEquals(2, recentTasks.size());
        assertEquals("T2", recentTasks.get(0).getTitle());
        assertEquals("T1", recentTasks.get(1).getTitle());
    }

    @Test
    void completeTaskAndReturnNext_shouldReturnNextTaskOrNull() {
        Task first = taskService.createTask("First", "Desc1");
        Task second = taskService.createTask("Second", "Desc2");

        Task next = taskService.completeTaskAndReturnNext(TaskId.fromLong(first.getId()));
        assertNotNull(next);
        assertEquals("Second", next.getTitle());

        Task nullNext = taskService.completeTaskAndReturnNext(TaskId.fromLong(second.getId()));
        assertNull(nullNext);
    }

    @Test
    void createTask_emptyTitle_shouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                taskService.createTask("", "Desc"));
        assertTrue(exception.getMessage().contains("Title cannot be blank"));
    }

    @Test
    void createTask_SQLInjection_shouldBeSafe() {
        Task task = taskService.createTask("Test'); DROP TABLE task; --", "Malicious");

        assertNotNull(task.getId());
        assertEquals(1, taskRepository.count());
    }

    @Test
    void createTask_XSS_shouldEscapeTitleAndDescription() {
        String title = "<script>alert('XSS')</script>";
        String description = "<b>bold</b>";

        Task task = taskService.createTask(title, description);

        // Persisted values remain raw
        assertEquals(title, task.getTitle());
        assertEquals(description, task.getDescription());
    }
}
