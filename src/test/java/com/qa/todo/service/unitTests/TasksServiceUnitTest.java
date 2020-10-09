package com.qa.todo.service.unitTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.qa.todo.dto.TasksDTO;
import com.qa.todo.persistence.domain.Tasks;
import com.qa.todo.persistence.repository.TasksRepository;
import com.qa.todo.service.TasksService;

@SpringBootTest
public class TasksServiceUnitTest 
{
    @Autowired
    private TasksService tasksService;
    
    @MockBean
    private TasksRepository tasksRepository;
    
    @MockBean
    private ModelMapper modelMapper;
    
    private List<Tasks> tasksList;
    private Tasks tasksTest;
    private Tasks tasksTestWithId;
    private TasksDTO tasksDTO;

    final Long id = 1l;
    private final String task = "Update Design Project";
    private final Boolean completed = false;
    
    @BeforeEach
    void init()
    {
    	this.tasksList = new ArrayList<>();
    	this.tasksTest = new Tasks(task, completed);
    	this.tasksList.add(tasksTest);
    	this.tasksTestWithId = new Tasks(tasksTest.getTask(), tasksTest.getCompleted());
        this.tasksTestWithId.setId(id);
        this.tasksDTO = modelMapper.map(tasksTestWithId, TasksDTO.class);
    }
    
    @Test
    void createTest()
    {
    	when(this.tasksRepository.save(this.tasksTest))
    	.thenReturn(this.tasksTestWithId);
    	when(this.modelMapper.map(this.tasksTestWithId, TasksDTO.class))
    	.thenReturn(this.tasksDTO);
        TasksDTO expected = this.tasksDTO;
        TasksDTO actual = this.tasksService.create(this.tasksTest);
        assertThat(expected).isEqualTo(actual);
        verify(this.tasksRepository, times(1)).save(this.tasksTest);
    }
    
    @Test
    void readTest()
    {
        when(this.tasksRepository.findById(this.id)).thenReturn(Optional.of(this.tasksTestWithId));
        when(this.modelMapper.map(tasksTestWithId, TasksDTO.class)).thenReturn(tasksDTO);
        assertThat(this.tasksDTO).isEqualTo(this.tasksService.read(this.id));
        verify(this.tasksRepository, times(1)).findById(this.id);
    }
    
    @Test
    void readAllTest()
    {
        when(this.tasksRepository.findAll()).thenReturn(this.tasksList);
        when(this.modelMapper.map(this.tasksTestWithId, TasksDTO.class)).thenReturn(tasksDTO);
        assertThat(this.tasksService.readAll().isEmpty()).isFalse();
        verify(this.tasksRepository, times(1)).findAll();
    }
    
    @Test
    void updateTest()
    {
        Tasks tasks = new Tasks("Complete Design Project", true);
        tasks.setId(this.id);
        TasksDTO tasksDTO = new TasksDTO(null, "Complete Design Project", true);
        Tasks updatedTasks = new Tasks(tasksDTO.getTask(), tasksDTO.getCompleted());
        updatedTasks.setId(this.id);
        TasksDTO updatedTasksDTO = new TasksDTO(this.id, updatedTasks.getTask(),
                updatedTasks.getCompleted());
        when(this.tasksRepository.findById(this.id)).thenReturn(Optional.of(tasks));
        when(this.tasksRepository.save(tasks)).thenReturn(updatedTasks);
        when(this.modelMapper.map(updatedTasks, TasksDTO.class))
        .thenReturn(updatedTasksDTO);
        TasksDTO expected = updatedTasksDTO;
        TasksDTO actual = this.tasksService.update(tasksDTO, this.id);
        assertThat(expected).isEqualTo(actual);
        verify(this.tasksRepository, times(1)).findById(1l);
        verify(this.tasksRepository, times(1)).save(updatedTasks);
    }
    
    @Test
    void deleteTest() 
    {
        when(this.tasksRepository.existsById(id)).thenReturn(true, false);
        assertThat(this.tasksService.delete(id)).isTrue();
        verify(this.tasksRepository, times(1)).deleteById(id);
        verify(this.tasksRepository, times(2)).existsById(id);
    }
}
