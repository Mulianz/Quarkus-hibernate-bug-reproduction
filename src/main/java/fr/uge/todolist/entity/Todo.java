package fr.uge.todolist.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;

    protected Todo() {
    }

    public Todo(String title) {
        Objects.requireNonNull(title);
        if (title.isBlank() || title.length() > 255) {
            throw new IllegalArgumentException("Title must contain between 1 and 255 characters.");
        }
        this.title = title;
        super();
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

}
