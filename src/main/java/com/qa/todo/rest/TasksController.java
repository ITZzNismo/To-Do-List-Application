package com.qa.todo.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qa.todo.dto.TasksDTO;
import com.qa.todo.persistence.domain.Tasks;
import com.qa.todo.service.TasksService;

@RestController
@CrossOrigin
@RequestMapping("/tasks")
public class TasksController 
{
	private TasksService service;

	public TasksController(TasksService service) {
		super();
		this.service = service;
	}
	
	// Create
    @PostMapping("/create")
    public ResponseEntity<TasksDTO> create(@RequestBody Tasks tasks) {
        TasksDTO created = this.service.create(tasks);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}