package com.tallerbd.backend;

import com.bedatadriven.jackson.datatype.jts.JtsModule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendApplication {

	@Bean
	public JtsModule jtsModule()
	{
		return new JtsModule();
	}

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
