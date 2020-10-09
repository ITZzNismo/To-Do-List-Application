package com.qa.todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class ToDoProjectApplication 
{
	public static void main(String[] args) 
	{
		SpringApplication.run(ToDoProjectApplication.class, args);
	}
}