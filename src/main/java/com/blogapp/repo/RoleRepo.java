package com.blogapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogapp.entities.Role;

public interface RoleRepo extends JpaRepository<Role, Long> {
	
	Role findByName(String name);

}
