package com.blogapp.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blogapp.dto.CategoryDto;
import com.blogapp.entities.Category;
import com.blogapp.repo.CategoryRepo;
import com.blogapp.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepo categoryRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CategoryDto createCategory(CategoryDto cdto) {

		// first fetch existing category from db by its name
		if (categoryRepo.existsByNameIgnoreCase(cdto.getName())) {

			throw new RuntimeException("Category Already Exists!!");
		}

		// convert dto to entity
		Category category = modelMapper.map(cdto, Category.class);

		Category savedCategory = categoryRepo.save(category);

		// convert entity to dto and return
		return modelMapper.map(savedCategory, CategoryDto.class);
	}

	@Override
	public List<CategoryDto> getAllCategory() {
		
		List<Category> categories = categoryRepo.findAll();
		
		//convert list of entity to list of dto
		List<CategoryDto> catDtoList = categories.stream()
												 .map(c -> modelMapper.map(c, CategoryDto.class))
												 .collect(Collectors.toList());
				
		return catDtoList;
	}

}
