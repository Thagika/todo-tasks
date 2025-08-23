package com.main.java.todo_app.service;

import com.main.java.todo_app.domain.Task;
import com.main.java.todo_app.domain.TaskId;
import com.main.java.todo_app.repository.TaskRepository;
import com.main.java.todo_app.validation.TaskValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DefaultTaskServiceTest {

    @Mock
    private TaskRepository repository;

    @Mock
    private TaskValidator validator;

    @InjectMocks
    private DefaultTaskService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTask_shouldSaveTask() {
        String title = "Test Task";
        String description = "Task Description";
        Task task = new Task(title, description);

        when(repository.save(any(Task.class))).thenReturn(task);

        Task result = service.createTask(title, description);

        assertEquals(title, result.getTitle());
        assertEquals(description, result.getDescription());
        verify(validator).ensureValidTitle(title);
        verify(validator).ensureValidDescription(description);
        verify(repository).save(any(Task.class));
    }

    @Test
    void completeTaskAndReturnNext_shouldCompleteAndReturnNextTask() {
        Task existing = new Task("Old Task", "Desc");
        existing.setId(1L);

        Task nextTask = new Task("Next Task", "Next Desc");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.findTop5ByCompletedFalseOrderByCreatedAtDesc())
                .thenReturn(Arrays.asList(nextTask));

        Task result = service.completeTaskAndReturnNext(TaskId.fromLong(1L));

        assertTrue(existing.isCompleted());
        assertEquals(nextTask, result);
        verify(repository).save(existing);
    }

    @Test
    void completeTaskAndReturnNext_noNextTask_returnsNull() {
        Task existing = new Task("Old Task", "Desc");
        existing.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.findTop5ByCompletedFalseOrderByCreatedAtDesc())
                .thenReturn(List.of());

        Task result = service.completeTaskAndReturnNext(TaskId.fromLong(1L));

        assertTrue(existing.isCompleted());
        assertNull(result);
        verify(repository).save(existing);
    }

    @Test
    void getRecentTasks_shouldReturnTop5Uncompleted() {
        Task task1 = new Task("Task 1", "Desc 1");
        Task task2 = new Task("Task 2", "Desc 2");

        when(repository.findTop5ByCompletedFalseOrderByCreatedAtDesc())
                .thenReturn(Arrays.asList(task1, task2));

        List<Task> result = service.getRecentTasks();

        assertEquals(2, result.size());
        assertEquals(task1, result.get(0));
        assertEquals(task2, result.get(1));
    }
}
