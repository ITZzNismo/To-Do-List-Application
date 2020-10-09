package com.qa.todo.service.unitTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
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
import com.qa.todo.dto.ToDoDTO;
import com.qa.todo.persistence.domain.ToDo;
import com.qa.todo.persistence.repository.ToDoRepository;
import com.qa.todo.service.ToDoService;

@SpringBootTest
public class ToDoServiceUnitTest 
{
	@Autowired
	private ToDoService todoservice;

	@MockBean
	private ToDoRepository todorepository;

	@MockBean
	private ModelMapper modelMapper;

	private List<ToDo> todoList;
	private ToDo todoTest;
	private ToDo todoTestWithId;
	private ToDoDTO todoDTO;

	final Long id = 1l;

	@BeforeEach
	void init() 
	{
		this.todoList = new ArrayList<>();
		this.todoTest = new ToDo("Simon Powell");
		todoList.add(todoTest);
		this.todoTestWithId = new ToDo(todoTest.getName());
		this.todoTestWithId.setId(this.id);
		this.todoDTO = modelMapper.map(todoTestWithId, ToDoDTO.class);
	}

	@Test
	void createTest() 
	{
		when(this.todorepository.save(this.todoTest)).thenReturn(this.todoTestWithId);
		when(this.modelMapper.map(this.todoTestWithId, ToDoDTO.class)).thenReturn(this.todoDTO);
		assertThat(this.todoDTO).isEqualTo(this.todoservice.create(this.todoTest));
		verify(this.todorepository, atLeastOnce()).save(this.todoTest);
	}

	@Test
	void readOneTest() 
	{
		when(this.todorepository.findById(this.id)).thenReturn(Optional.of(this.todoTestWithId));
		when(this.modelMapper.map(todoTestWithId, ToDoDTO.class)).thenReturn(this.todoDTO);
		assertThat(this.todoDTO).isEqualTo(this.todoservice.read(this.id));
		verify(this.todorepository, atLeastOnce()).findById(this.id);
	}

	@Test
	void readAllTest() 
	{
		when(this.todorepository.findAll()).thenReturn(this.todoList);
		when(this.modelMapper.map(this.todoTestWithId, ToDoDTO.class)).thenReturn(this.todoDTO);
		assertThat(this.todoservice.readAll().isEmpty()).isFalse();
		verify(this.todorepository, atLeastOnce()).findAll();
	}

	@Test
	void updateTest() 
	{
		final List<TasksDTO> tasksList = new ArrayList<>();
		final String name = "John Smith";

		ToDo oldEntity = new ToDo(name);
		oldEntity.setId(this.id);
		ToDoDTO oldDTO = new ToDoDTO(null, name, tasksList);

		ToDo newEntity = new ToDo(oldEntity.getName());
		newEntity.setId(this.id);
		ToDoDTO newDTO = new ToDoDTO(this.id, newEntity.getName(), oldDTO.getTasks());

		when(this.todorepository.findById(this.id)).thenReturn(Optional.of(oldEntity));
		when(this.todorepository.save(oldEntity)).thenReturn(newEntity);
		when(this.modelMapper.map(newEntity, ToDoDTO.class)).thenReturn(newDTO);
		assertThat(newDTO).isEqualTo(this.todoservice.update(oldDTO, this.id));
		verify(this.todorepository, atLeastOnce()).findById(this.id);
		verify(this.todorepository, atLeastOnce()).save(newEntity);
	}

	@Test
	void deleteTest() 
	{
		when(this.todorepository.existsById(this.id)).thenReturn(true, false);
		assertThat(this.todoservice.delete(this.id)).isTrue();
		verify(this.todorepository, atLeastOnce()).deleteById(this.id);
		verify(this.todorepository, atLeastOnce()).existsById(this.id);
	}
}
