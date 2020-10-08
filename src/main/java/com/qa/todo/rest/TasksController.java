package com.qa.todo.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

	public TasksController(TasksService service) 
	{
		super();
		this.service = service;
	}
	
	// Create
    @PostMapping("/create")
    public ResponseEntity<TasksDTO> create(@RequestBody Tasks tasks) 
    {
        TasksDTO created = this.service.create(tasks);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    
    // Read All
    @GetMapping("/read")
    public ResponseEntity<List<TasksDTO>> readAll()
    {
    	List<TasksDTO> readAll = this.service.readAll();
    	return new ResponseEntity<>(readAll, HttpStatus.OK);
    }
    
   
    // Read 
    @GetMapping("/read/{id}")
    public ResponseEntity<TasksDTO> read(@PathVariable Long id)
    {
    	TasksDTO read = this.service.read(id);
    	return new ResponseEntity<>(read, HttpStatus.OK);
    }
    
    // Update
    @PutMapping("/update/{id}")
    public ResponseEntity<TasksDTO> update(@PathVariable Long id, @RequestBody TasksDTO tasksDTO)
    {
    	TasksDTO update = this.service.update(tasksDTO, id);
    	return new ResponseEntity<>(update, HttpStatus.ACCEPTED);
    }
    
    // Delete
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<TasksDTO> delete(@PathVariable Long id) 
    {
        return this.service.delete(id) ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
 }