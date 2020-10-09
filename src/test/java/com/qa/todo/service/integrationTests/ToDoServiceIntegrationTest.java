package com.qa.todo.service.integrationTests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.qa.todo.dto.TasksDTO;
import com.qa.todo.dto.ToDoDTO;
import com.qa.todo.persistence.domain.ToDo;
import com.qa.todo.persistence.repository.ToDoRepository;
import com.qa.todo.service.ToDoService;

@SpringBootTest
public class ToDoServiceIntegrationTest 
{
	@Autowired
	private ToDoService todoservice;

	@Autowired
	private ToDoRepository todorepository;

	@Autowired
	private ModelMapper modelMapper;

	private ToDoDTO mapToDTO(ToDo todo) 
	{
		return this.modelMapper.map(todo, ToDoDTO.class);
	}

	private final List<TasksDTO> tasks = new ArrayList<>();
	private ToDo todoTest;
	private ToDo todoTestWithId;
	private ToDoDTO todoDTO;

	private Long id;

	private final String name = "Simon Powell";

	private final String updatedName = "John Smith";

	@BeforeEach
	void init() 
	{
		this.todorepository.deleteAll();
		this.todoTest = new ToDo(this.name);
		this.todoTestWithId = this.todorepository.save(this.todoTest);
		this.todoDTO = this.mapToDTO(this.todoTestWithId);
		this.id = this.todoTestWithId.getId();
	}

	@Test
	void createTest() 
	{
		assertThat(this.todoDTO).isEqualTo(this.todoservice.create(this.todoTest));
	}

	@Test
	void readOneTest() 
	{
		assertThat(this.todoDTO).isEqualTo(this.todoservice.read(this.id));
	}

	@Test
	void readAllTest() 
	{
		assertThat(Stream.of(this.todoDTO).collect(Collectors.toList())).isEqualTo(this.todoservice.readAll());
	}

	@Test
	void updateTest() 
	{
		ToDoDTO oldDTO = new ToDoDTO(null, this.updatedName, this.tasks);
		ToDoDTO newDTO = new ToDoDTO(this.id, oldDTO.getName(), oldDTO.getTasks());

		assertThat(newDTO).isEqualTo(this.todoservice.update(oldDTO, this.id));
	}

	@Test
	void deleteTest() 
	{
		assertThat(this.todoservice.delete(this.id)).isTrue();
	}
}
