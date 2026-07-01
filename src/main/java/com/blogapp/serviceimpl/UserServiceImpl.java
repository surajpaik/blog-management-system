package com.blogapp.serviceimpl;

import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blogapp.dto.UserSignUpDto;
import com.blogapp.entities.MyUser;
import com.blogapp.entities.Role;
import com.blogapp.repo.RoleRepo;
import com.blogapp.repo.UserRepo;
import com.blogapp.services.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private RoleRepo roleRepo;
	
	
	@Override
	public UserSignUpDto saveUser(UserSignUpDto dto) {
		
		//check email is already exists or not
		MyUser guestUserExists = userRepo.findByEmail(dto.getEmail());
		
		if(guestUserExists != null) {
			
			throw new RuntimeException("User already Exists!!" + dto.getEmail());
		}
		
		//convert dto to entity
		MyUser guestUser = mapper.map(dto, MyUser.class);
		
		//convert plain password to bycryptpassword
		guestUser.setPassword(encoder.encode(dto.getPassword()));
		
		//by default all user have guest role
		Role role = roleRepo.findByName("GUEST");
		
		List<Role> roleList = Arrays.asList(role);
		
		//fetch role from db and convert it into as a list and then set it to my user
		guestUser.setRoles(roleList);
		
		//save my user entity to db
		MyUser  savedUser = userRepo.save(guestUser);
		
		//entity to dto
		
		return mapper.map(savedUser, UserSignUpDto.class);
	}
	
	
	

}
