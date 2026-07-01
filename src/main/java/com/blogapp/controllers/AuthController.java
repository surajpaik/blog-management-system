package com.blogapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.blogapp.dto.UserSignUpDto;
import com.blogapp.services.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserService userService;

	// to display login form
	@GetMapping("/customlogin")
	public String showCustomLoginForm(Model model) {

		return "login";
	}

	// to display signup form
	@GetMapping("/signup")
	public String showSignupForm(Model model) {

		// create signupdto and bind to sign up form
		model.addAttribute("user", new UserSignUpDto());

		return "signup";
	}

	// to save new user
	@PostMapping("/saveuser")
	public String saveUser(@ModelAttribute("user") @Valid UserSignUpDto userDto, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		
		//validation check
		if(result.hasErrors()) {
			
			model.addAttribute("user", userDto);
			return "signup";
		}
		// calling user service
		try {
			userService.saveUser(userDto);
			
		}
		catch(Exception e) {
			
			result.rejectValue("email", null, "Email Already exists");
			model.addAttribute("user", userDto);
			return "signup";
		}
		
		redirectAttributes.addFlashAttribute("msg", "User Registered!");
		
		return "redirect:/auth/signup";

	}

}
