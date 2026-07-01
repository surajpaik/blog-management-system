package com.blogapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogapp.entities.MyUser;

public interface UserRepo extends JpaRepository<MyUser, Long> {
	
	MyUser findByEmail(String email);

}
