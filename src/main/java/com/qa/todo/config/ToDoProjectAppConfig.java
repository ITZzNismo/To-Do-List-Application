package com.qa.todo.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

@Configuration
@Profile({"dev"})
public class ToDoProjectAppConfig 
{
	@Bean
	@Scope("prototype")
	public ModelMapper mapper() 
	{
	    return new ModelMapper();
	}
}