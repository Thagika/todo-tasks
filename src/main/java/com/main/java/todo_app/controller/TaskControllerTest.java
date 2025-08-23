package com.main.java.todo_app.controller;

import com.main.java.todo_app.domain.Task;
import com.main.java.todo_app.domain.TaskId;
import com.main.java.todo_app.service.TaskService;
import com.main.java.todo_app.web.dto.CreateTaskRequest;
import com.main.java.todo_app.web.dto.TaskResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTask_shouldReturnTaskResponse() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Title");
        request.setDescription("Desc");

        Task task = new Task("Title", "Desc");

        when(taskService.createTask("Title", "Desc")).thenReturn(task);

        ResponseEntity<TaskResponse> response = controller.createTask(request);

        assertEquals(task.getTitle(), response.getBody().getTitle());
        assertEquals(task.getDescription(), response.getBody().getDescription());
        verify(taskService).createTask("Title", "Desc");
    }

    @Test
    void completeTask_shouldReturnNextTaskResponse() {
        Task nextTask = new Task("Next", "Next Desc");

        when(taskService.completeTaskAndReturnNext(TaskId.fromLong(1L))).thenReturn(nextTask);

        ResponseEntity<TaskResponse> response = controller.completeTask("1");

        assertEquals(nextTask.getTitle(), response.getBody().getTitle());
        assertEquals(nextTask.getDescription(), response.getBody().getDescription());
        verify(taskService).completeTaskAndReturnNext(TaskId.fromLong(1L));
    }

    @Test
    void completeTask_noNextTask_returnsNullBody() {
        when(taskService.completeTaskAndReturnNext(TaskId.fromLong(1L))).thenReturn(null);

        ResponseEntity<TaskResponse> response = controller.completeTask("1");

        assertNull(response.getBody());
        verify(taskService).completeTaskAndReturnNext(TaskId.fromLong(1L));
    }

    @Test
    void getRecentTasks_shouldReturnTop5Responses() {
        Task t1 = new Task("T1", "D1");
        Task t2 = new Task("T2", "D2");

        when(taskService.getRecentTasks()).thenReturn(Arrays.asList(t1, t2));

        ResponseEntity<List<TaskResponse>> response = controller.getRecentTasks();

        assertEquals(2, response.getBody().size());
        assertEquals("T1", response.getBody().get(0).getTitle());
        assertEquals("T2", response.getBody().get(1).getTitle());
        verify(taskService).getRecentTasks();
    }
}
