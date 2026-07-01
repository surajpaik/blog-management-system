package com.blogapp.helper;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.blogapp.entities.MyUser;
import com.blogapp.entities.Role;
import com.blogapp.repo.RoleRepo;
import com.blogapp.repo.UserRepo;




@Component
public class DataInitlizerRunner implements CommandLineRunner {

	private static final Logger logger =
			LoggerFactory.getLogger(DataInitlizerRunner.class);

	@Value("${app.admin.email}")
	private String adminEmail;

	@Value("${app.admin.password}")
	private String adminPassword;
	
	@Autowired
	private RoleRepo roleRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private PasswordEncoder encoder;

	@Override
	public void run(String... args) throws Exception {
		
		//save role to db
		createRoleIfNotExists("ADMIN");
		createRoleIfNotExists("GUEST");
		
		// save admin to db
		createAdminIfNotExists();
		
	}
	
	//to create Roles
	private void createRoleIfNotExists(String roleName) {
		
		//first check role is already exists or not
		Role role = roleRepo.findByName(roleName);
		
		if(role == null) {
			
			Role userRole = new Role();
			userRole.setName(roleName);
			roleRepo.save(userRole);
			logger.info("Role '{}' created successfully.", roleName);
		}
	}
	
	//to create admin user
	private void createAdminIfNotExists() {
		
		//check if admin already exists
		MyUser existingAdminUser = userRepo.findByEmail(adminEmail);
		
		if(existingAdminUser == null) {
			
			MyUser adminUser = new MyUser();
			adminUser.setFirstName("myadmin");
			adminUser.setLastName("user");
			adminUser.setEmail(adminEmail);
			String encodedPass = encoder.encode(adminPassword);
			adminUser.setPassword(encodedPass);
			
			// we need list of roles
			Role role = roleRepo.findByName("ADMIN");
			
			List<Role> roleList = Arrays.asList(role);
			
			adminUser.setRoles(roleList);
			
			userRepo.save(adminUser);

			logger.info("Default admin user created successfully.");
		}
	}
	

}
