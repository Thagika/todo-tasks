package com.main.java.todo_app.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskValidatorTest {

    private TaskValidator validator;

    @BeforeEach
    void setUp() {
        validator = new TaskValidator();
    }

    @Test
    void validTitlePasses() {
        assertDoesNotThrow(() -> validator.ensureValidTitle("Valid Task Title"));
    }

    @Test
    void blankTitleThrows() {
        assertThrows(IllegalArgumentException.class, () -> validator.ensureValidTitle(""));
    }

    @Test
    void nullTitleThrows() {
        assertThrows(IllegalArgumentException.class, () -> validator.ensureValidTitle(null));
    }

    @Test
    void tooLongTitleThrows() {
        String longTitle = "A".repeat(201);
        assertThrows(IllegalArgumentException.class, () -> validator.ensureValidTitle(longTitle));
    }

    @Test
    void validDescriptionPasses() {
        assertDoesNotThrow(() -> validator.ensureValidDescription("Valid Description"));
    }

    @Test
    void tooLongDescriptionThrows() {
        String longDescription = "A".repeat(1001);
        assertThrows(IllegalArgumentException.class, () -> validator.ensureValidDescription(longDescription));
    }
}
