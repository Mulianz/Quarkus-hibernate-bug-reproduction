package fr.uge.todolist.controller;

import fr.uge.todolist.dto.TodoRequest;
import fr.uge.todolist.dto.TodoResponse;
import fr.uge.todolist.service.TodoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.List;
import java.util.Objects;

@Path("/todos")
public class TodoController {

    private final TodoService service;

    @Inject
    public TodoController(TodoService service) {
        Objects.requireNonNull(service);
        this.service = service;
        super();
    }

    @GET
    public List<TodoResponse> findAll() {
        return service.findAll().stream().map(t -> new TodoResponse(t.getId(), t.getTitle())).toList();
    }

    @POST
    public TodoResponse create(TodoRequest request) {
        if (request == null) {
            throw new BadRequestException("Request is required.");
        }
        var todo = service.create(request.title());
        return new TodoResponse(todo.getId(), todo.getTitle());
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") long id) {
        if (id < 0) {
            throw new BadRequestException("Id must be positive");
        }
        service.delete(id);
    }

}
