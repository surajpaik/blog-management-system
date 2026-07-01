package com.blogapp.controllers;

import java.io.File;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.blogapp.dto.CategoryDto;
import com.blogapp.dto.PostCommentDto;
import com.blogapp.dto.PostDto;
import com.blogapp.helper.Helper;
import com.blogapp.services.CategoryService;
import com.blogapp.services.CommentService;
import com.blogapp.services.PostService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/post")
public class PostController {

	@Autowired
	private PostService postService;
	
	@Autowired
	private CommentService commentService;

	@Autowired
	private CategoryService categoryService;

	// display all post page for admin/guest
	@GetMapping("/allpost")
	public String allPosts(@RequestParam(defaultValue = "0") int pageNo, Model model) {

		int size = 5;

		Page<PostDto> postPage = postService.getPostsForCurrentUser(pageNo, size);

		model.addAttribute("postPage", postPage);
		model.addAttribute("currentPage", pageNo);

		return "common/allposts";
	}

	// it display and add post page
	@GetMapping("/newpost")
	public String showCreatePostPage(Model model) {

		// fetch list of category from db
		List<CategoryDto> categoriesdto = categoryService.getAllCategory();

		PostDto pdto = new PostDto();
		model.addAttribute("post", pdto);

		model.addAttribute("categories", categoriesdto);

		return "common/createpost";
	}

	@PostMapping("/savepost")
	public String savePost(@ModelAttribute("post") @Valid PostDto postDto, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, @RequestParam("image") MultipartFile imagePart, Model model) {
		// check validation error
		if (bindingResult.hasErrors()) {

			List<CategoryDto> categoriesdto = categoryService.getAllCategory();
			model.addAttribute("categories", categoriesdto);

			model.addAttribute("post", postDto);

			return "common/createpost";
		}

		// change title in hypen style
		String convertedTitle = Helper.getUrlFromTitle(postDto.getTitle());
		postDto.setUrl(convertedTitle);

		String imageName = "";

		if (!imagePart.isEmpty()) {

			imageName = StringUtils.cleanPath(Objects.requireNonNull(imagePart.getOriginalFilename()));
			postDto.setPostImageName(imageName);
		} else {

			redirectAttributes.addFlashAttribute("error", "Please upload the files!!");

			return "redirect:/post/newpost";

		}

		// save post data and get post id
		PostDto savedPostDto = postService.savePost(postDto);

		// get post id from savedpostdto
		if (savedPostDto != null) {

			String uploadDirectorypath = "postfiles/" + savedPostDto.getId();

			boolean isImageSaved = Helper.saveFile(uploadDirectorypath, imageName, imagePart);

			if (isImageSaved) {

				redirectAttributes.addFlashAttribute("msg", "postAdded!!");
			} else {

				redirectAttributes.addFlashAttribute("error", "Something went wrong, image not saved!!");
			}
		} else {

			redirectAttributes.addFlashAttribute("error", "Something went wrong!!");
		}
		// save post image to folder

		// add Flash attribute for UI msg

		return "redirect:/post/newpost";
	}

	// approve post
	@GetMapping("/admin/approvePost")
	public String approvePost(@RequestParam("postId") int postId) {

		// call service and service call repo
		postService.updatePostStatus(postId, "APPROVED");

		return "redirect:/post/allpost";
	}

	// reject post
	@GetMapping("/admin/rejectPost")
	public String rejectPost(@RequestParam("postId") int postId) {

		// call service and service call repo
		postService.updatePostStatus(postId, "REJECTED");

		return "redirect:/post/allpost";
	}
	
	//to display view post
	@GetMapping("/viewpostdata")
	public String viewPost(@RequestParam("postId") int postId, Model model) {
		
		PostDto postdto = postService.getPostById(postId);
		model.addAttribute("post", postdto);
		return "common/viewpost";
	}
	
	
	//delete post 
	@GetMapping("/deletepost")
	public String deletePost(@RequestParam("postId") int postId) {
		
		//based on post id delete post image from postfiles dir
		String folderPath="postfiles/"+postId; //postfiles/6
		
		File file = new File(folderPath);
		
		if(file.exists()) {
			
			// delete data from sub folder
			for(File f : file.listFiles()) {
				
				f.delete();
			}
			//delete the sub folder
			file.delete();
		}
		//delete post from db
		postService.deletePostbyId(postId);
		
		return "redirect:/post/allpost";
	}
	//display edit post page with existing data
	@GetMapping("/editpostdata")
	public String editPost(@RequestParam("postId") int postId, Model model) {
		
		// fetch list of category from db
		List<CategoryDto> categoriesdto = categoryService.getAllCategory();
		
		//fetch existing post from db and add to model which will show existing data from db
		PostDto pdto = postService.getPostById(postId);
		model.addAttribute("post", pdto);

		model.addAttribute("categories", categoriesdto);

		return "common/editpost";
		
	}
	
	//update existing post

	@PostMapping("/updatepost")
	public String updatePost(@ModelAttribute("post") @Valid PostDto postDto, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, @RequestParam("image") MultipartFile imagePart, Model model) {
		// check validation error
		if (bindingResult.hasErrors()) {

			List<CategoryDto> categoriesdto = categoryService.getAllCategory();
			model.addAttribute("categories", categoriesdto);

			model.addAttribute("post", postDto);

			return "common/editpost";
		}
		
		//check user uploaded the post image or not if uploaded the new image then delete old image
		if(imagePart != null && !imagePart.isEmpty()) {
			
			//delete old image by using oldimagename
			if(postDto.getPostImageName() != null) {
				
				String oldImagePath = "postfiles/"+postDto.getId()+"/"+postDto.getPostImageName();
				
				File oldImageFile =  new File(oldImagePath);
				if(oldImageFile.exists()) {
					
					System.out.println("old image deleted");
					oldImageFile.delete();
				}
			}
			//now after deleting old image file save new image file to dir
			String newimageName = StringUtils.cleanPath(Objects.requireNonNull(imagePart.getOriginalFilename()));
			postDto.setPostImageName(newimageName);
			
			
			String uploadDirectorypath = "postfiles/" + postDto.getId();

			boolean isImageSaved = Helper.saveFile(uploadDirectorypath, newimageName, imagePart);

			if (isImageSaved) {

				redirectAttributes.addFlashAttribute("msg", "postAdded!!");
			} else {

				redirectAttributes.addFlashAttribute("error", "Something went wrong, image not saved!!");
			}
		}
		
		
		//update post data to db
		PostDto updatedPost = postService.updatePost(postDto);
		
		if(updatedPost != null) {
			
			redirectAttributes.addFlashAttribute("msg", "Post Updated!!");
		}
		else {

			redirectAttributes.addFlashAttribute("error", "Something went wrong!!");
		}
		
		
		return "redirect:/post/allpost";
	}

	//to display all comments only admin can see thise
	@GetMapping("/admin/viewAllcomments")
	public String viewAllComments(@RequestParam(defaultValue = "0") int pageNo, Model model) {
		
		int size = 5;

		Pageable pageable = PageRequest.of(pageNo, size, Sort.by("createdOn").descending());
		Page<PostCommentDto> commentPage = commentService.getAllComments(pageable);
		
		model.addAttribute("commentPage", commentPage);
		
		model.addAttribute("currentPage", pageNo);
		
		return "admin/allcomments";
	}
	
	//delete comment by comment id
	@GetMapping("/admin/delete-comment/{id}")
	public String deleteComment(@PathVariable("id") Long id) {
		
		commentService.deleteComment(id);
		
		return "redirect:/post/admin/viewAllcomments";
	}
	
	@GetMapping("/test-error")
	public String testError() {

	    throw new RuntimeException("This is a test exception.");

	}
}
