package com.qa.todo.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qa.todo.dto.ToDoDTO;
import com.qa.todo.persistence.domain.ToDo;
import com.qa.todo.service.ToDoService;

@RestController
@RequestMapping("/todo")
public class ToDoController 
{
	private ToDoService service;
	
	@Autowired
	public ToDoController(ToDoService service) 
	{
		super();
		this.service = service;
	}
	
	@PostMapping("/create")
	public ResponseEntity<ToDoDTO> create(@RequestBody ToDo todo)
	{
		ToDoDTO created = this.service.create(todo);
		return new ResponseEntity<>(created, HttpStatus.CREATED);
	}
	
	@GetMapping("/readAll")
	public ResponseEntity<List<ToDoDTO>> readAll()
	{
		List<ToDoDTO> readAll = this.service.readAll();
		return new ResponseEntity<>(readAll, HttpStatus.OK);
	}
	
	@GetMapping("/read/{id}")
	public ResponseEntity<ToDoDTO> read(@PathVariable Long id)
	{
		ToDoDTO read = this.service.read(id);
		return new ResponseEntity<>(read, HttpStatus.OK);
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<ToDoDTO> update(@PathVariable Long id, @RequestBody ToDoDTO todoDTO)
	{
    	ToDoDTO update = this.service.update(todoDTO, id);
    	return new ResponseEntity<>(update, HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ToDoDTO> delete(@PathVariable Long id)
	{
        return this.service.delete(id) ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}