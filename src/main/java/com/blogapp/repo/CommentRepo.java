package com.blogapp.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogapp.entities.PostComment;

public interface CommentRepo extends JpaRepository<PostComment, Long> {
	
	List<PostComment> findByPost_IdOrderByCreatedOnDesc(int postId);
	
	long countByPostId(int postId);

}
