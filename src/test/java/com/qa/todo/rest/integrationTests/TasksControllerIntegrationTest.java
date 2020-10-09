package com.qa.todo.rest.integrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.todo.dto.TasksDTO;
import com.qa.todo.persistence.domain.Tasks;
import com.qa.todo.persistence.repository.TasksRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class TasksControllerIntegrationTest 
{
	@Autowired
	private MockMvc mockmvc;
	
	@Autowired
	private TasksRepository tasksrepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired 
	private ObjectMapper objectMapper;
	
    private Tasks tasksTest;
    private Tasks tasksTestWithId;
    private TasksDTO tasksDTO;
    
    private Long id;
    private final String task = "Update Design Project";
    private final Boolean completed = true;
    
    private TasksDTO mapToDTO(Tasks tasks) 
    {
        return this.modelMapper.map(tasks, TasksDTO.class);
    }
    
    @BeforeEach
    void init()
    {
    	this.tasksrepository.deleteAll();
    	this.tasksTest = new Tasks(this.task, this.completed);
        this.tasksTestWithId = this.tasksrepository.save(this.tasksTest);
        this.tasksDTO = this.mapToDTO(this.tasksTestWithId);
        this.id = this.tasksTestWithId.getId();
    }
    
    @Test
    void createTest() throws Exception
    {
    	this.mockmvc
    		.perform(request(HttpMethod.POST, "/tasks/create")
    		.contentType(MediaType.APPLICATION_JSON)
    		.content(this.objectMapper.writeValueAsString(this.tasksTest))
    		.accept(MediaType.APPLICATION_JSON))
    		.andExpect(status().isCreated())
    		.andExpect(content().json(this.objectMapper.writeValueAsString(this.tasksDTO)));
    }
    
    @Test
    void readTest() throws Exception
    {
    	this.mockmvc
    		.perform(request(HttpMethod.GET, "/tasks/read/" + this.id).accept(MediaType.APPLICATION_JSON))
    		.andExpect(status().isOk())
    		.andExpect(content().json(this.objectMapper.writeValueAsString(this.tasksDTO)));
    }
    
    @Test
    void readAllTest() throws Exception
    {
    	List<TasksDTO> tasksList = new ArrayList<>();
    	tasksList.add(this.tasksDTO);
    	String expected = this.objectMapper.writeValueAsString(tasksList);	
    	String actual = this.mockmvc
    		.perform(request(HttpMethod.GET, "/tasks/readAll/")
    		.accept(MediaType.APPLICATION_JSON))
    		.andExpect(status().isOk())
    		.andReturn().getResponse().getContentAsString();
    	assertEquals(expected, actual);
    }
    
    @Test
    void updateTest() throws Exception
    {
    	TasksDTO newTasks = new TasksDTO(null, "Complete Design Project", false);
    	Tasks updatedTasks = new Tasks(newTasks.getTask(), newTasks.getCompleted());
    	updatedTasks.setId(this.id);
    	String expected = this.objectMapper.writeValueAsString(this.mapToDTO(updatedTasks));
    	String actual = this.mockmvc
        		.perform(request(HttpMethod.PUT, "/tasks/update/" + this.id)
        	    .contentType(MediaType.APPLICATION_JSON)
        	    .content(this.objectMapper.writeValueAsString(newTasks))
        		.accept(MediaType.APPLICATION_JSON))
        		.andExpect(status().isAccepted())
        		.andReturn().getResponse().getContentAsString();
    	assertEquals(expected, actual);
    }
    
    @Test
    void testDelete() throws Exception 
    {
        this.mockmvc.perform(request(HttpMethod.DELETE, "/tasks/delete/" + this.id))
        .andExpect(status().isNoContent());
    }
}
