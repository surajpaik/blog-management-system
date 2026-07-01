package com.blogapp.serviceimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blogapp.dto.PostDto;
import com.blogapp.entities.Category;
import com.blogapp.entities.MyUser;
import com.blogapp.entities.Post;
import com.blogapp.helper.Helper;
import com.blogapp.repo.CategoryRepo;
import com.blogapp.repo.CommentRepo;
import com.blogapp.repo.PostRepository;
import com.blogapp.repo.UserRepo;
import com.blogapp.securityconfig.SecurityHelper;
import com.blogapp.services.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepository postRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private CommentRepo commentRepo;

	@Override
	public PostDto savePost(PostDto pdto) {

		// convert dto to entity

		Post postEntity = modelMapper.map(pdto, Post.class);

		postEntity.setStatus("PENDING");

		// before saving post set category to post entity
		// based on category id fetch category object/data from db and set to post
		Category category = categoryRepo.findById(pdto.getCategoryId())
				.orElseThrow(() -> new RuntimeException("Category not found invalid id!!"));

		postEntity.setCategory(category);
		
		//we need to set currently logged in user to post
		String username = SecurityHelper.getCurrentLoggedInUsername();
		MyUser currentLoggedInUser = userRepo.findByEmail(username);
		
		postEntity.setUser(currentLoggedInUser);
		
		

		Post savedPost = postRepo.save(postEntity);

		// entity to dto
		PostDto dto = modelMapper.map(savedPost, PostDto.class);

		dto.setId(savedPost.getId());

		return dto;
	}

	@Override
	public Page<PostDto> getPostsForCurrentUser(int pageNo, int size) {

		Pageable pageable = PageRequest.of(pageNo, size, Sort.by("createdOn").descending());


		// security logic
		//get currently logged in user name
		String username = SecurityHelper.getCurrentLoggedInUsername();
		
		//fetch logged in user from db
		MyUser loggedInUser = userRepo.findByEmail(username);
		
		//fetch logged in user roles
		boolean isAdmin = loggedInUser.getRoles().stream().anyMatch(role->role.getName().equals("ADMIN"));
		
		Page<Post> postPage;
		
		if(isAdmin) {
			
			//if admin logged in then fetch all posts
			postPage = postRepo.findAll(pageable);
		}else {
			
			//if guest then fetch specific post
			postPage = postRepo.findByUser(loggedInUser, pageable);
		}

		Page<PostDto> postPageDto = postPage.map(p -> modelMapper.map(p, PostDto.class));

		return postPageDto;
	}

	@Override
	public PostDto getPostById(int postId) {

		Post post = postRepo.findById(postId).orElseThrow(() -> new RuntimeException("post not found invalid id!!"));

		PostDto dto = modelMapper.map(post, PostDto.class);

		return dto;
	}

	@Override
	public void deletePostbyId(int postId) {

		Post post = postRepo.findById(postId).orElseThrow(() -> new RuntimeException("post not found invalid id!!"));
		postRepo.delete(post);

	}

	@Override
	public PostDto updatePost(PostDto newpost) {

		Post existingpost = postRepo.findById(newpost.getId())
				.orElseThrow(() -> new RuntimeException("post not found invalid id!!"));

		existingpost.setTitle(newpost.getTitle());
		existingpost.setShortDesc(newpost.getShortDesc());
		existingpost.setContent(newpost.getContent());
		existingpost.setPostImageName(newpost.getPostImageName());

		Category category = categoryRepo.findById(newpost.getCategoryId())
				.orElseThrow(() -> new RuntimeException("Category not found invalid id!!"));

		existingpost.setCategory(category);

		// save the updated data to db
		Post entity = postRepo.save(existingpost);

		return modelMapper.map(entity, PostDto.class);
	}

	@Override
	public void updatePostStatus(int postId, String status) {

		Post existingPost = postRepo.findById(postId)
				.orElseThrow(() -> new RuntimeException("post not found invalid id!!"));

		existingPost.setStatus(status);

		postRepo.save(existingPost);

	}

	@Override
	public Page<PostDto> getallApprovedBlogPost(Pageable pageable) {
		
		Page<Post> approvedPosts = postRepo.findByStatus("APPROVED", pageable);
		
		Page<PostDto> approvedPostDto = approvedPosts.map(p ->{
				PostDto dto = modelMapper.map(p, PostDto.class);
				dto.setCategoryName(p.getCategory().getName());
				dto.setAuthorName(
				        p.getUser().getFirstName() + " " +
				        p.getUser().getLastName()
				);
				dto.setShortDesc(Helper.htmlSanitize(dto.getShortDesc()));
				
				// Set total comments
				dto.setCommentCount(commentRepo.countByPostId(p.getId()));
				
				dto.setReadingTime(
				        Helper.calculateReadingTime(p.getContent())
				);
				
				return dto;
		});
		
		return approvedPostDto;
	}

	@Override
	public Page<PostDto> getPostsByCategory(int catId, Pageable pageable) {

		Page<Post> postPage = postRepo.findPostsByCategoryAndStatus(catId, "APPROVED", pageable);
		
		Page<PostDto> approvedPostDto = postPage.map(p ->{
			PostDto dto = modelMapper.map(p, PostDto.class);
			dto.setCategoryName(p.getCategory().getName());
			dto.setAuthorName(
			        p.getUser().getFirstName() + " " +
			        p.getUser().getLastName()
			);
			dto.setShortDesc(Helper.htmlSanitize(dto.getShortDesc()));
			
			dto.setCommentCount(commentRepo.countByPostId(p.getId()));
			
			dto.setReadingTime(
			        Helper.calculateReadingTime(p.getContent())
			);
			return dto;
	});
	
	return approvedPostDto;
		
	}

	@Override
	public Page<PostDto> searchBlogPost(String search, Pageable pageable) {

		Page<Post> postPage = postRepo.searchPost(search, "APPROVED", pageable);
		Page<PostDto> searchedPost = postPage.map(p ->{
			PostDto dto = modelMapper.map(p, PostDto.class);
			dto.setCategoryName(p.getCategory().getName());
			dto.setAuthorName(
			        p.getUser().getFirstName() + " " +
			        p.getUser().getLastName()
			);
			dto.setShortDesc(Helper.htmlSanitize(dto.getShortDesc()));
			dto.setCommentCount(commentRepo.countByPostId(p.getId()));
			
			dto.setReadingTime(
			        Helper.calculateReadingTime(p.getContent())
			);
			return dto;
		});
		
		return searchedPost;
	}

	@Override
	public PostDto getPostByIdandUrl(int id, String url) {
		
		Post post = postRepo.findByIdAndUrl(id, url).orElseThrow(() -> new RuntimeException("post not found invalid id!!"));
		
		return modelMapper.map(post, PostDto.class);
	}
	
	

}
