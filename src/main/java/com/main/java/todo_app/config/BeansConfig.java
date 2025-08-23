package com.main.java.todo_app.config;

import com.main.java.todo_app.repository.TaskRepository;
import com.main.java.todo_app.service.DefaultTaskService;
import com.main.java.todo_app.service.TaskService;
import com.main.java.todo_app.validation.TaskValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {

    @Bean
    public TaskValidator taskValidator() {
        return new TaskValidator();
    }
}
