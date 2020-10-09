package com.qa.todo.rest.unitTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.qa.todo.dto.TasksDTO;
import com.qa.todo.persistence.domain.Tasks;
import com.qa.todo.rest.TasksController;
import com.qa.todo.service.TasksService;

@SpringBootTest
public class TasksControllerUnitTest 
{
	@Autowired
	private TasksController taskscontroller;
	
	@MockBean
	private TasksService tasksservice;
	
	private TasksDTO tasksDTO;
	
	@Autowired
	private ModelMapper modelMapper;
	
	private TasksDTO mapToDTO(Tasks tasks) 
	{
		return this.modelMapper.map(tasks, TasksDTO.class);
	}
	
	private List<Tasks> tasksList;
	private Tasks tasksTest;
	private Tasks tasksTestWithId;
	
	// Default values for testing
	private final Long id = 1l;
    private final String task = "Update Design Project";
    private final Boolean completed = true;
   
    @BeforeEach
    void init()
    {
    	this.tasksList = new ArrayList<>();
    	this.tasksTest = new Tasks(this.task, this.completed);
    	this.tasksTestWithId = new Tasks(tasksTest.getTask(), tasksTest.getCompleted());
    	this.tasksTestWithId.setId(this.id);
    	this.tasksList.add(tasksTestWithId);
    	this.tasksDTO = this.mapToDTO(tasksTestWithId);
    }
    
    @Test
    void testCreate()
    {
    	when(this.tasksservice.create(tasksTest)).thenReturn(this.tasksDTO);
    	
    	TasksDTO testCreated = this.tasksDTO;
    	ResponseEntity<TasksDTO> actual = new ResponseEntity<TasksDTO>(testCreated, HttpStatus.CREATED);
    	ResponseEntity<TasksDTO> expected = this.taskscontroller.create(tasksTest);
    	assertThat(actual).isEqualTo(expected);
    }
    
    @Test
    void testRead()
    {
    	when(this.tasksservice.read(this.id)).thenReturn(this.tasksDTO);
    	TasksDTO testReadOne = this.tasksDTO;
    	ResponseEntity<TasksDTO> actual = new ResponseEntity<TasksDTO>(testReadOne, HttpStatus.OK);
    	ResponseEntity<TasksDTO> expected = this.taskscontroller.read(this.id);
    	assertThat(actual).isEqualTo(expected);
    }
    
    @Test
    void testReadAll()
    {
    	when(this.tasksservice.readAll()).thenReturn(this.tasksList.stream().map(this::mapToDTO).collect(Collectors.toList()));
        assertThat(this.taskscontroller.readAll().getBody().isEmpty()).isFalse();
        verify(this.tasksservice, times(1)).readAll();
    }
    
    @Test
    void updateTest() 
    {
        TasksDTO newTasks = new TasksDTO(null, "Complete Design Project", false);
        TasksDTO newTasksWithId = new TasksDTO(this.id, newTasks.getTask(), newTasks.getCompleted());

        when(this.tasksservice.update(newTasks, this.id)).thenReturn(newTasksWithId);
    	ResponseEntity<TasksDTO> actual = new ResponseEntity<TasksDTO>(newTasksWithId, HttpStatus.ACCEPTED);
    	ResponseEntity<TasksDTO> expected = this.taskscontroller.update(this.id, newTasks);
    	assertThat(actual).isEqualTo(expected);
        verify(this.tasksservice, times(1)).update(newTasks, this.id);
    }

    @Test
    void deleteTest() 
    {
        this.taskscontroller.delete(id);
        verify(this.tasksservice, times(1)).delete(id);
    }
}
