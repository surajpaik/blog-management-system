package com.blogapp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.blogapp.dto.CategoryDto;
import com.blogapp.dto.PostCommentDto;
import com.blogapp.dto.PostDto;
import com.blogapp.services.CategoryService;
import com.blogapp.services.CommentService;
import com.blogapp.services.PostService;

import jakarta.validation.Valid;

@Controller
public class BlogPostClientController {

	@Autowired
	private PostService postService;
	
	@Autowired
	private CommentService commentService;

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/")
	public String showAllBlogPost(@RequestParam(defaultValue = "0") int pageNo, Model model) {

		int size = 6;
		// fetch all approved post from db
		Pageable pageable = PageRequest.of(pageNo, size, Sort.by("createdOn").descending());
		Page<PostDto> blogPage = postService.getallApprovedBlogPost(pageable);

		// fetch all categories from db
		List<CategoryDto> categories = categoryService.getAllCategory();

		model.addAttribute("blogPage", blogPage);

		model.addAttribute("categories", categories);

		model.addAttribute("currentPage", pageNo);

		model.addAttribute("selectedcategoryid", null);
		

		return "blog/allblogs";
	}

	@GetMapping("/category/{id}")
	public String getPostByCategoryId(@PathVariable("id") int categoryId, @RequestParam(defaultValue = "0") int pageNo,
			Model model) {

		int size = 6;

		Pageable pageable = PageRequest.of(pageNo, size, Sort.by("createdOn").descending());
		Page<PostDto> blogPage = postService.getPostsByCategory(categoryId, pageable);

		// fetch all categories from db
		List<CategoryDto> categories = categoryService.getAllCategory();

		model.addAttribute("blogPage", blogPage);

		model.addAttribute("categories", categories);

		model.addAttribute("currentPage", pageNo);

		model.addAttribute("selectedcategoryid", categoryId);
		
		
		return "blog/allblogs";

	}
	@GetMapping("/searchblogpost")
	public String searchBlogPost(@RequestParam("search") String search, @RequestParam(defaultValue = "0") int pageNo,
			Model model) {

		int size = 6;

		Pageable pageable = PageRequest.of(pageNo, size, Sort.by("createdOn").descending());
		if (search != null && !search.isEmpty()) {

			// fetch post from db
			Page<PostDto> blogPage = postService.searchBlogPost(search, pageable);

			List<CategoryDto> categories = categoryService.getAllCategory();

			model.addAttribute("blogPage", blogPage);

			model.addAttribute("categories", categories);

			model.addAttribute("currentPage", pageNo);
			
			model.addAttribute("selectedcategoryid", null);
			
			model.addAttribute("search", search);
		} else {
			return "redirect:/";
		}

		return "blog/allblogs";
	}
	@GetMapping("/blogpost/{id}/{url}")
	public String viewSpecificPost(@PathVariable("id") int id, @PathVariable("url") String url, Model model) {
		
		PostDto postDto = postService.getPostByIdandUrl(id, url);
		
		model.addAttribute("post", postDto);
		
		//comment dto object
		model.addAttribute("comment", new PostCommentDto());
		
		//fetch all comments to display in ui
		List<PostCommentDto> comments = commentService.getCommentsByPostId(id);
		
		model.addAttribute("comments", comments);
		
		return "blog/viewblog";
		
	}
	
	@PostMapping("/addcomment")
	public String saveComment(@ModelAttribute("comment") @Valid PostCommentDto commentDto, BindingResult result, Model model) {
		
		
		if(result.hasErrors()) {
			
			PostDto postDto = postService.getPostById(commentDto.getPostId());
			
			model.addAttribute("post", postDto);
			
			model.addAttribute("comment", commentDto);
			
			return "blog/viewblog";
		}
		
		//save comment
		commentService.addComment(commentDto);
		
		return "redirect:/blogpost/"+commentDto.getPostId()+"/"+commentDto.getUrl();
	}

}
