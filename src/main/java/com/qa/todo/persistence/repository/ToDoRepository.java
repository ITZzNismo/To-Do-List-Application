package com.qa.todo.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qa.todo.persistence.domain.ToDo;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Long>
{

}
