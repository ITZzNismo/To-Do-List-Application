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
import com.qa.todo.dto.ToDoDTO;
import com.qa.todo.persistence.domain.ToDo;
import com.qa.todo.rest.ToDoController;
import com.qa.todo.service.ToDoService;

@SpringBootTest
public class ToDoControllerUnitTest 
{
    @Autowired
    private ToDoController todocontroller;

    @MockBean
    private ToDoService todoservice;

    @Autowired
    private ModelMapper modelMapper;

    private ToDoDTO mapToDTO(ToDo todo) 
    {
        return this.modelMapper.map(todo, ToDoDTO.class);
    }

    private List<ToDo> todoList;
    private ToDo todoTest;
    private ToDo todoTestWithId;
    private ToDoDTO todoDTO;

    private final Long id = 1l;
    private final String name = "Simon Powell";

    @BeforeEach
    void init() 
    {
        this.todoList = new ArrayList<>();
        this.todoTest = new ToDo(name);
        this.todoTestWithId = new ToDo(todoTest.getName());
        this.todoTestWithId.setId(id);
        this.todoList.add(this.todoTestWithId);
    }

    @Test
    void createTest() throws Exception 
    {
        when(this.todoservice.create(this.todoTest)).thenReturn(this.todoDTO);
        assertThat(new ResponseEntity<ToDoDTO>(this.todoDTO, HttpStatus.CREATED))
                .isEqualTo(this.todocontroller.create(this.todoTest));
        verify(this.todoservice, times(1)).create(this.todoTest);
    }

    @Test
    void readOneTest() throws Exception 
    {
        when(this.todoservice.read(this.id)).thenReturn(this.todoDTO);
        assertThat(new ResponseEntity<ToDoDTO>(this.todoDTO, HttpStatus.OK)).isEqualTo(this.todocontroller.read(this.id));
        verify(this.todoservice, times(1)).read(this.id);
    }

    @Test
    void readAllTest() throws Exception 
    {
        when(this.todoservice.readAll()).thenReturn(this.todoList.stream().map(this::mapToDTO).collect(Collectors.toList()));
        assertThat(this.todocontroller.readAll().getBody().isEmpty()).isFalse();
        verify(this.todoservice, times(1)).readAll();
    }

    @Test
    void updateTest() throws Exception 
    {
        final List<TasksDTO> tasks = new ArrayList<>();
        ToDoDTO oldToDo = new ToDoDTO(null, "John Smith", tasks);
        ToDoDTO newToDo = new ToDoDTO(this.id, oldToDo.getName(), oldToDo.getTasks());

        when(this.todoservice.update(oldToDo, this.id)).thenReturn(newToDo);
        assertThat(new ResponseEntity<ToDoDTO>(newToDo, HttpStatus.ACCEPTED))
                .isEqualTo(this.todocontroller.update(this.id, oldToDo));
        verify(this.todoservice, times(1)).update(oldToDo, this.id);
    }

    @Test
    void deleteTest() throws Exception 
    {
        this.todocontroller.delete(this.id);
        verify(this.todoservice, times(1)).delete(this.id);
    }
}