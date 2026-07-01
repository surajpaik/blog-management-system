package com.blogapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogapp.entities.Category;

public interface CategoryRepo extends JpaRepository<Category, Long>{
	
	//method for fetching category by name


	boolean existsByNameIgnoreCase(String name);

}
