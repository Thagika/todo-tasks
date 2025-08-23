package com.main.java.todo_app.validation;

import org.springframework.stereotype.Component;

@Component
public class TaskValidator {

    private static final int MAX_TITLE_LENGTH = 200;
    private static final int MAX_DESCRIPTION_LENGTH = 1000;

    public void ensureValidTitle(String title) {
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Title cannot be blank");
        if (title.length() > MAX_TITLE_LENGTH)
            throw new IllegalArgumentException("Title cannot exceed " + MAX_TITLE_LENGTH + " characters");
    }

    public void ensureValidDescription(String description) {
        if (description != null && description.length() > MAX_DESCRIPTION_LENGTH)
            throw new IllegalArgumentException("Description cannot exceed " + MAX_DESCRIPTION_LENGTH + " characters");
    }
}
