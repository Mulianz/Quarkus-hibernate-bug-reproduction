package fr.uge.todolist.dto;

import java.util.Objects;

public record TodoResponse(long id, String title) {

    public TodoResponse {
        if (id < 0) {
            throw new IllegalArgumentException("Id must be positive");
        }
        Objects.requireNonNull(title);
    }

}
