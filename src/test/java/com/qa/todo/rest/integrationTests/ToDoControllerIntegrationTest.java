package com.qa.todo.rest.integrationTests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.todo.dto.ToDoDTO;
import com.qa.todo.persistence.domain.ToDo;
import com.qa.todo.persistence.repository.ToDoRepository;
import com.qa.todo.rest.ToDoController;

@SpringBootTest
@AutoConfigureMockMvc
public class ToDoControllerIntegrationTest 
{
    private static final MediaType jsonFormat = MediaType.APPLICATION_JSON;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ToDoRepository todorepository;

    @Autowired
    private ObjectMapper objectmapper;

    private Long id;
    private ToDo todoTest;
    private ToDo todoTestWithId;
    private ToDoDTO todoDTO;

    @BeforeEach
    void init() 
    {
        this.todorepository.deleteAll();
        this.todoTest = new ToDo("Simon Powell");
        this.todoTestWithId = this.todorepository.saveAndFlush(this.todoTest);
        this.id = this.todoTestWithId.getId();
        this.todoDTO = new ToDoDTO(this.id, this.todoTest.getName(), new ArrayList<>());
    }

    @Test
    void testCreate() throws Exception 
    {
        this.mvc.perform(post("/todo/create")
                .accept(jsonFormat)
                .contentType(jsonFormat)
                .content(this.objectmapper.writeValueAsString(this.todoTest)))
            .andExpect(status().isCreated())
            .andExpect(content().json(this.objectmapper.writeValueAsString(this.todoDTO)));
    }

    @Test
    void testReadOne() throws Exception 
    {
        this.mvc.perform(get("/todo/read/" + this.id)
                .accept(jsonFormat)
                .contentType(jsonFormat))
            .andExpect(status().isOk())
            .andExpect(content().json(this.objectmapper.writeValueAsString(this.todoDTO)));
    }

    @Test
    void testReadAll() throws Exception 
    {
        final List<ToDo> TODO = new ArrayList<>();
        TODO.add(this.todoTestWithId);
        this.mvc.perform(get("/todo/readAll")
                .accept(jsonFormat)
                .contentType(jsonFormat))
            .andExpect(status().isOk())
            .andExpect(content().json(this.objectmapper.writeValueAsString(TODO)));
    }

    @Test
    void testUpdate() throws Exception 
    {
        final ToDoDTO NEW_TODO_DTO = new ToDoDTO(this.id, "John Smith", new ArrayList<>());
        
        this.mvc.perform(put("/todo/update/" + this.id)
                .accept(jsonFormat)
                .contentType(jsonFormat)
                .content(this.objectmapper.writeValueAsString(NEW_TODO_DTO)))
            .andExpect(status().isAccepted())
            .andExpect(content().json(this.objectmapper.writeValueAsString(NEW_TODO_DTO)));
    }
    
    @Test
    void testDelete() throws Exception 
    {
        this.mvc
            .perform(delete("/todo/delete/" + this.id)
                    .accept(jsonFormat)
                    .contentType(jsonFormat)
                    .content(this.objectmapper.writeValueAsString(this.todoTest)))
            .andExpect(status().isNoContent());
    }
}