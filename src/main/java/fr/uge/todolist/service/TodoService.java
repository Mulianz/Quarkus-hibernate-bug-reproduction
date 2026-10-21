package fr.uge.todolist.service;

import fr.uge.todolist.entity.Todo;
import fr.uge.todolist.repository.TodoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Objects;

@ApplicationScoped
public final class TodoService {

    private final TodoRepository repository;

    @Inject
    public TodoService(TodoRepository repository) {
        super();
        Objects.requireNonNull(repository);
        this.repository = repository;
    }

    @Transactional
    public List<Todo> findAll() {
        return repository.findAll();
    }

    @Transactional
    public Todo create(String title) {
        Objects.requireNonNull(title);
        var todo = new Todo(title.strip());
        return repository.insert(todo);
    }

    @Transactional
    public void delete(long id) {
        if (id < 0) {
            throw new IllegalArgumentException("Id must be positive");
        }
        repository.delete(id);
    }
}
