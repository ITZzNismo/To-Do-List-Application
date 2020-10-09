package com.qa.todo.service.integrationTests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.qa.todo.dto.TasksDTO;
import com.qa.todo.persistence.domain.Tasks;
import com.qa.todo.persistence.repository.TasksRepository;
import com.qa.todo.service.TasksService;

@SpringBootTest
public class TasksServiceIntegrationTest 
{
    @Autowired
    private TasksService tasksService;

    @Autowired
    private TasksRepository tasksRepository;

    @Autowired
    private ModelMapper modelMapper;

    private TasksDTO mapToDTO(Tasks tasks) 
    {
        return this.modelMapper.map(tasks, TasksDTO.class);
    }
    
    private Tasks tasksTest;
    private Tasks tasksTestWithId;
    private TasksDTO testTasksDTO;

    private Long id;
    private final String task = "Update Design Project";
    private final Boolean completed = false;
    
    @BeforeEach
    void init()
    {
    this.tasksRepository.deleteAll();
    this.tasksTest = new Tasks(this.task, this.completed);
    this.tasksTestWithId = this.tasksRepository.save(this.tasksTest);
    this.testTasksDTO = this.mapToDTO(tasksTestWithId);
    this.id = this.tasksTestWithId.getId();
    }
    
    @Test
    void createTest()
    {
    	TasksDTO actual = this.testTasksDTO;
    	TasksDTO expected = this.tasksService.create(tasksTest);
    	assertThat(actual).isEqualTo(expected);
    }

    @Test 
    void readTest()
    {
    	TasksDTO actual = this.testTasksDTO;
    	TasksDTO expected = this.tasksService.read(this.id);
    	assertThat(actual).isEqualTo(expected);
    }
    
    @Test
    void readAllTest()
    {
        assertThat(this.tasksService.readAll())
        .isEqualTo(Stream.of(this.testTasksDTO)
                .collect(Collectors.toList()));
    }
    
    @Test
    void testUpdate() 
    {
        TasksDTO newTasks = new TasksDTO(null, "Complete Design Project", true);
        TasksDTO actual = new TasksDTO(this.id, newTasks.getTask(), 
        		newTasks.getCompleted());
        TasksDTO expected = this.tasksService.update(newTasks, this.id);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testDelete() 
    {
        assertThat(this.tasksService.delete(this.id)).isTrue();
    }
}
