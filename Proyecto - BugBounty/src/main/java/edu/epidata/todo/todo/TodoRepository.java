package edu.epidata.todo.todo;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TodoRepository extends CrudRepository<ToDo, UUID> {
}
