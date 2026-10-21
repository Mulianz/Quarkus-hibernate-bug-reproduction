package fr.uge.todolist.dto;

import java.util.Objects;

public record TodoRequest(String title) {
    public TodoRequest {
        Objects.requireNonNull(title);
    }

}

