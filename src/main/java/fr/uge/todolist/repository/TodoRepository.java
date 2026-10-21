package fr.uge.todolist.repository;

import fr.uge.todolist.entity.Todo;
import jakarta.data.repository.Insert;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

import java.util.List;

@Repository
public interface TodoRepository {

    @Query("select t from Todo t order by t.id")
    List<Todo> findAll();

    @Insert
    Todo insert(Todo todo);

    @Query("delete from Todo t where t.id = :id")
    void delete(long id);
}
