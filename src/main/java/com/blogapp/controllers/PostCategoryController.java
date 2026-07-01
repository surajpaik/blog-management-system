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

import com.blogapp.dto.CategoryDto;
import com.blogapp.services.CategoryService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/category")
public class PostCategoryController {

	@Autowired
	private CategoryService categoryService;

	// to display add category page
	@GetMapping("/addcategory")
	public String showCategoryForm(Model model) {

		model.addAttribute("category", new CategoryDto());
		return "admin/postcategory";
	}

	@PostMapping("/save")
	public String saveCategory(@ModelAttribute("category") @Valid CategoryDto categoryDto, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Model model) {

		if (bindingResult.hasErrors()) {

			model.addAttribute("category", categoryDto);
			return "admin/postcategory";
		}

		try {

			categoryService.createCategory(categoryDto);

			redirectAttributes.addFlashAttribute("msg", "category added!!");

		} catch (RuntimeException e) {

			redirectAttributes.addFlashAttribute("error", "category already exists!!");

			return "redirect:/admin/category/addcategory";

		}

		return "redirect:/admin/category/addcategory";
	}

}
