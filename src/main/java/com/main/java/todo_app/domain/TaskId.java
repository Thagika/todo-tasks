package com.main.java.todo_app.domain;

public final class TaskId {
    private final Long value;

    private TaskId(Long value) {
        if (value == null) throw new IllegalArgumentException("TaskId cannot be null");
        this.value = value;
    }

    public static TaskId fromLong(Long value) {
        return new TaskId(value);
    }

    public static TaskId fromString(String value) {
        return new TaskId(Long.parseLong(value));
    }

    public Long getValue() { return value; }

    @Override
    public String toString() { return String.valueOf(value); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskId)) return false;
        return value.equals(((TaskId) o).value);
    }

    @Override
    public int hashCode() { return value.hashCode(); }
}
