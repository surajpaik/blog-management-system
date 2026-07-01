package com.blogapp.services;

import java.util.List;

import com.blogapp.dto.CategoryDto;

public interface CategoryService {
	
	CategoryDto createCategory(CategoryDto cdto);
	
	List<CategoryDto> getAllCategory();

}
