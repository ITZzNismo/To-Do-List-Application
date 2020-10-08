package com.qa.todo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.qa.todo.dto.ToDoDTO;
import com.qa.todo.exception.ToDoNotFoundException;
import com.qa.todo.persistence.domain.ToDo;
import com.qa.todo.persistence.repository.ToDoRepository;
import com.qa.todo.utils.ToDoProjectBeanUtils;

public class ToDoService 
{
    private ToDoRepository repository;
    
    private ModelMapper mapper;

	public ToDoService(ToDoRepository repository, ModelMapper mapper) 
	{
		super();
		this.repository = repository;
		this.mapper = mapper;
	}
	
    private ToDoDTO mapToDTO(ToDo todo) 
    {
        return this.mapper.map(todo, ToDoDTO.class);
    }

    private ToDo mapFromDTO(ToDoDTO todoDTO) 
    {
        return this.mapper.map(todoDTO, ToDo.class);
    }
    
    public ToDoDTO create(ToDo todo) 
    {
        ToDo created = this.repository.save(todo);
        ToDoDTO mapped = this.mapToDTO(created);
        return mapped;
    }

    public List<ToDoDTO> readAll() 
    {
        List<ToDo> found = this.repository.findAll();
        List<ToDoDTO> streamed = found.stream()
        		.map(this::mapToDTO)
        		.collect(Collectors.toList());
        return streamed;
    }

    public ToDoDTO read(Long id) 
    {
        ToDo found = this.repository.findById(id)
        		.orElseThrow(ToDoNotFoundException::new);
        return this.mapToDTO(found);
    }

    public ToDoDTO update(ToDoDTO ToDoDTO, Long id) 
    {
        ToDo toUpdate = this.repository.findById(id)
        		.orElseThrow(ToDoNotFoundException::new);
        ToDoProjectBeanUtils.mergeNotNull(ToDoDTO, toUpdate);
        return this.mapToDTO(this.repository.save(toUpdate));
    }

    public boolean delete(Long id) 
    {
        if (!this.repository.existsById(id)) 
        {
            throw new ToDoNotFoundException();
        }
        this.repository.deleteById(id);
        return !this.repository.existsById(id);
    }
}