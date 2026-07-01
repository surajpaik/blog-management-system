package com.blogapp.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.blogapp.entities.MyUser;
import com.blogapp.repo.CategoryRepo;
import com.blogapp.repo.CommentRepo;
import com.blogapp.repo.PostRepository;
import com.blogapp.repo.UserRepo;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
	
	
	@Autowired
	private PostRepository postRepo;

	@Autowired
	private CategoryRepo categoryRepo;

	@Autowired
	private CommentRepo commentRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@GetMapping("/admin")
	public String showAdminDashboard(Model model) {

	    long totalPosts = postRepo.count();

	    long totalCategories = categoryRepo.count();

	    long totalComments = commentRepo.count();
	    
	    long totalUsers = userRepo.count();
	    
	    long approvedPosts = postRepo.countByStatus("APPROVED");
	    
	    long pendingPosts = postRepo.countByStatus("PENDING");

	    model.addAttribute("pendingPosts", pendingPosts);

	    model.addAttribute("approvedPosts", approvedPosts);

	    model.addAttribute("totalUsers", totalUsers);

	    model.addAttribute("totalPosts", totalPosts);

	    model.addAttribute("totalCategories", totalCategories);

	    model.addAttribute("totalComments", totalComments);

	    return "admin/admindashboard";
	}
	
	@GetMapping("/guest")
	public String showGuestDashboard(Model model,
	                                 Principal principal) {

	    String email = principal.getName();

	    MyUser user = userRepo.findByEmail(email);

	    model.addAttribute("myPosts",
	            postRepo.countByUser(user));

	    model.addAttribute("approvedPosts",
	            postRepo.countByUserAndStatus(user, "approved"));

	    model.addAttribute("pendingPosts",
	            postRepo.countByUserAndStatus(user, "pending"));

	    return "guest/guestdashboard";
	}
}
