package com.blogapp.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.blogapp.dto.PostCommentDto;
import com.blogapp.entities.Post;
import com.blogapp.entities.PostComment;
import com.blogapp.repo.CommentRepo;
import com.blogapp.repo.PostRepository;
import com.blogapp.services.CommentService;

@Service
public class PostCommentServiceImpl implements CommentService {

	@Autowired
	private CommentRepo commentRepo;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private PostRepository postRepo;

	@Override
	public void addComment(PostCommentDto dto) {

		// fetch specific post based on id
		Post post = postRepo.findById(dto.getPostId())
				.orElseThrow(() -> new RuntimeException("Post not found Invalid id"));

		PostComment commententity = mapper.map(dto, PostComment.class);

		commententity.setPost(post);
		commentRepo.save(commententity);

	}

	@Override
	public List<PostCommentDto> getCommentsByPostId(int postId) {

		List<PostComment> comments = commentRepo.findByPost_IdOrderByCreatedOnDesc(postId);

		List<PostCommentDto> commentDtos = comments.stream().map(c -> mapper.map(c, PostCommentDto.class))
				.collect(Collectors.toList());
		return commentDtos;
	}

	@Override
	public Page<PostCommentDto> getAllComments(Pageable pageable) {
		
		Page<PostComment> comments = commentRepo.findAll(pageable);
		
		Page<PostCommentDto> commentDto = comments.map(c->mapper.map(c,PostCommentDto.class));
		
		
		return commentDto;
	}

	@Override
	public void deleteComment(Long id) {
		
		commentRepo.deleteById(id);
		
	}
	
	

}
