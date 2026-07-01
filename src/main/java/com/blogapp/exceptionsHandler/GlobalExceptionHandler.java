package com.blogapp.exceptionsHandler;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(RuntimeException.class)
	public String handlerRuntimeException(RuntimeException ex, Model model) {
		
		model.addAttribute("error", ex.getMessage());
		
		return "error-page";
	}

}
