package com.main.java.todo_app.controller;

import com.main.java.todo_app.domain.Task;
import com.main.java.todo_app.repository.TaskRepository;
import com.main.java.todo_app.web.dto.CreateTaskRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanDatabase() {
        taskRepository.deleteAll();
    }

    // ✅ Existing Tests
    @Test
    void createTask_withXSS_shouldStoreRawValuesAndEscapeForHtml() throws Exception {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("XSS Test");
        request.setDescription("<script>alert('XSS')</script>");

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("XSS Test")))
                .andExpect(jsonPath("$.description", is("<script>alert('XSS')</script>")))
                .andExpect(jsonPath("$.titleForHtml", is("XSS Test")))
                .andExpect(jsonPath("$.descriptionForHtml", is("&lt;script&gt;alert('XSS')&lt;/script&gt;")));
    }

    @Test
    void getRecentTasks_shouldReturnTasksInCorrectOrderWithEscaping() throws Exception {
        Task task1 = new Task("T1", "D1");
        Task task2 = new Task("T2", "D2");

        taskRepository.save(task1);
        taskRepository.save(task2);

        mockMvc.perform(get("/tasks/recent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("T2")))
                .andExpect(jsonPath("$[0].description", is("D2")))
                .andExpect(jsonPath("$[0].titleForHtml", is("T2")))
                .andExpect(jsonPath("$[0].descriptionForHtml", is("D2")))
                .andExpect(jsonPath("$[1].title", is("T1")))
                .andExpect(jsonPath("$[1].description", is("D1")))
                .andExpect(jsonPath("$[1].titleForHtml", is("T1")))
                .andExpect(jsonPath("$[1].descriptionForHtml", is("D1")));
    }

    @Test
    void completeTask_shouldPreserveRawValues() throws Exception {
        Task task1 = new Task("RawTitle", "<b>RawDescription</b>");
        Task task2 = new Task("NextTask", "NextDesc");

        taskRepository.save(task1);
        taskRepository.save(task2);

        mockMvc.perform(post("/tasks/" + task1.getId() + "/complete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("NextTask")))
                .andExpect(jsonPath("$.description", is("NextDesc")))
                .andExpect(jsonPath("$.titleForHtml", is("NextTask")))
                .andExpect(jsonPath("$.descriptionForHtml", is("NextDesc")));
    }

    @Test
    void createTask_withSQLInjectionAttempt_shouldStoreRawButEscapeOnOutput() throws Exception {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Injection");
        request.setDescription("1'; DROP TABLE tasks; --");

        String escaped = org.apache.commons.text.StringEscapeUtils.escapeHtml4("1'; DROP TABLE tasks; --");

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                // raw values
                .andExpect(jsonPath("$.title", is("Injection")))
                .andExpect(jsonPath("$.description", is("1'; DROP TABLE tasks; --")))
                // escaped values
                .andExpect(jsonPath("$.titleForHtml", is("Injection")))
                .andExpect(jsonPath("$.descriptionForHtml", is(escaped)));
    }


    @Test
    void createTask_withEmptyValues_shouldFailValidation() throws Exception {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("");
        request.setDescription("");

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTask_withLongDescription_shouldRespectValidationAndEscape() throws Exception {
        String longDesc = "A".repeat(1000);

        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Long Task");
        request.setDescription(longDesc);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                // raw values
                .andExpect(jsonPath("$.title", is("Long Task")))
                .andExpect(jsonPath("$.description", is(longDesc)))
                // escaped values (same here because "A" has no HTML entities)
                .andExpect(jsonPath("$.titleForHtml", is("Long Task")))
                .andExpect(jsonPath("$.descriptionForHtml", is(longDesc)));
    }

    @Test
    void createTask_withVeryLongValues_shouldFailValidation() throws Exception {
        String longText = "a".repeat(5000); // exceeds @Size limits

        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle(longText);
        request.setDescription(longText);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()); 
    }


    @Test
    void createTask_withPreEscapedHtml_shouldNotDoubleEscape() throws Exception {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Escaped");
        request.setDescription("&lt;b&gt;bold&lt;/b&gt;");

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("&lt;b&gt;bold&lt;/b&gt;")))
                .andExpect(jsonPath("$.descriptionForHtml", is("&amp;lt;b&amp;gt;bold&amp;lt;/b&amp;gt;")));
    }

    @Test
    void concurrentTaskCreation_shouldHandleConsistency() throws ExecutionException, InterruptedException {
        CreateTaskRequest r1 = new CreateTaskRequest();
        r1.setTitle("Task1");
        r1.setDescription("Desc1");

        CreateTaskRequest r2 = new CreateTaskRequest();
        r2.setTitle("Task2");
        r2.setDescription("Desc2");

        CompletableFuture<Void> f1 = CompletableFuture.runAsync(() -> {
            try {
                mockMvc.perform(post("/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(r1)))
                        .andExpect(status().isOk());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        CompletableFuture<Void> f2 = CompletableFuture.runAsync(() -> {
            try {
                mockMvc.perform(post("/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(r2)))
                        .andExpect(status().isOk());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        CompletableFuture.allOf(f1, f2).get();

        // assert both are saved
        assertThat(taskRepository.findAll(), hasSize(2));
    }
}
