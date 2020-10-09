package com.qa.todo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.todo.dto.TasksDTO;
import com.qa.todo.exception.TasksNotFoundException;
import com.qa.todo.persistence.domain.Tasks;
import com.qa.todo.persistence.repository.TasksRepository;
import com.qa.todo.utils.ToDoProjectBeanUtils;

@Service
public class TasksService 
{
    private TasksRepository repository;

    private ModelMapper mapper;

    @Autowired
    public TasksService(TasksRepository repository, ModelMapper mapper) 
    {
        this.repository = repository;
        this.mapper = mapper;
    }
    
    private TasksDTO mapToDTO(Tasks tasks) 
    {
        return this.mapper.map(tasks, TasksDTO.class);
    }

    private Tasks mapFromDTO(TasksDTO tasksDTO) 
    {
        return this.mapper.map(tasksDTO, Tasks.class);
    }
    
    // Create
    public TasksDTO create(Tasks tasks) 
    {
        Tasks created = this.repository.save(tasks);
        TasksDTO mapped = this.mapToDTO(created);
        return mapped;
    }
    
    // Read All
    public List<TasksDTO> readAll() 
    {
    	// Try findAllById(id)
        List<Tasks> found = this.repository.findAll();
        List<TasksDTO> streamed = found.stream()
        		.map(this::mapToDTO)
        		.collect(Collectors.toList());
        return streamed;
    }
    
    // Read
    public TasksDTO read(Long id) 
    {
        Tasks found = this.repository.findById(id)
        		.orElseThrow(TasksNotFoundException::new);
        return this.mapToDTO(found);
    }
    
    // Update
    public TasksDTO update(TasksDTO tasksDTO, Long id) 
    {
        Tasks toUpdate = this.repository.findById(id)
        		.orElseThrow(TasksNotFoundException::new);
        ToDoProjectBeanUtils.mergeNotNull(tasksDTO, toUpdate);
        return this.mapToDTO(this.repository.save(toUpdate));
    }
    
    // Delete
    public boolean delete(Long id) {
        if (!this.repository.existsById(id)) {
            throw new TasksNotFoundException();
        }
        this.repository.deleteById(id);
        return !this.repository.existsById(id);
    }
}