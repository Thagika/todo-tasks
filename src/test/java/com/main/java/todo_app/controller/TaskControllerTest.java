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
        task.setId(1L); // Set ID to avoid null issues

        when(taskService.createTask("Title", "Desc")).thenReturn(task);

        ResponseEntity<TaskResponse> response = controller.createTask(request);

        assertNotNull(response.getBody());
        assertEquals(task.getTitle(), response.getBody().getTitle());
        assertEquals(task.getDescription(), response.getBody().getDescription());
        verify(taskService).createTask("Title", "Desc");
    }

    @Test
    void completeTask_shouldReturnNextTaskResponse() {
        Task nextTask = new Task("Next", "Next Desc");
        nextTask.setId(2L);

        // Create a TaskId object
        TaskId taskId = TaskId.fromLong(1L);

        // Mock the service
        when(taskService.completeTaskAndReturnNext(taskId)).thenReturn(nextTask);

        // Call controller
        ResponseEntity<TaskResponse> response = controller.completeTask("1");

        assertNotNull(response.getBody());
        assertEquals(nextTask.getTitle(), response.getBody().getTitle());
        assertEquals(nextTask.getDescription(), response.getBody().getDescription());

        verify(taskService).completeTaskAndReturnNext(taskId);
    }

    @Test
    void completeTask_noNextTask_returnsNullBody() {
        TaskId taskId = TaskId.fromLong(1L);
        when(taskService.completeTaskAndReturnNext(taskId)).thenReturn(null);

        ResponseEntity<TaskResponse> response = controller.completeTask("1");

        assertNull(response.getBody());
        verify(taskService).completeTaskAndReturnNext(taskId);
    }


    @Test
    void getRecentTasks_shouldReturnTop5Responses() {
        Task t1 = new Task("T1", "D1");
        t1.setId(1L);
        Task t2 = new Task("T2", "D2");
        t2.setId(2L);

        when(taskService.getRecentTasks()).thenReturn(Arrays.asList(t1, t2));

        ResponseEntity<List<TaskResponse>> response = controller.getRecentTasks();

        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("T1", response.getBody().get(0).getTitle());
        assertEquals("T2", response.getBody().get(1).getTitle());
        verify(taskService).getRecentTasks();
    }
}
