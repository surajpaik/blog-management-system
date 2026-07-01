package com.blogapp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.blogapp.dto.PostDto;

public interface PostService {
	
	//save
	 PostDto savePost(PostDto pdto);
	 
	 //used to get all post base on guest or admin role
	 Page<PostDto> getPostsForCurrentUser(int pageNo, int size);
	 
	 //to update post status
	 void updatePostStatus(int postId, String status);
	 
	 //get post by id
	 PostDto getPostById(int postId);
	 
	 //delete post
	 void deletePostbyId(int postId);
	 
	 //update post
	 PostDto updatePost(PostDto postdto);
	 
	 //to get all approved posts
	 Page<PostDto> getallApprovedBlogPost(Pageable pageable);
	 
	 Page<PostDto> getPostsByCategory( int catId, Pageable pageable);
	 
	 Page<PostDto> searchBlogPost(String search, Pageable pageable);
	 
	 PostDto getPostByIdandUrl(int id, String url);

}
