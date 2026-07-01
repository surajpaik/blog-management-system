package com.blogapp.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.blogapp.dto.PostCommentDto;

public interface CommentService {
	
	void addComment(PostCommentDto dto);
	
	List<PostCommentDto> getCommentsByPostId(int postId);
	
	Page<PostCommentDto> getAllComments(Pageable pageable);
	
	void deleteComment(Long id);

}
