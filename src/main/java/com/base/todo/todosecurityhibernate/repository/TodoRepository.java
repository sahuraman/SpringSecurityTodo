package com.base.todo.todosecurityhibernate.repository;

import com.base.todo.todosecurityhibernate.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findByCompletedFalse();

    List<Todo> findByTitleContainingIgnoreCase(String title);
}   