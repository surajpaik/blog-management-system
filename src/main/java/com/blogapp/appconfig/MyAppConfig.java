package com.blogapp.appconfig;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyAppConfig {
	
	@Bean
	ModelMapper modelMapper() {
		
		return new ModelMapper();
		
	}

}
