package com.blogapp.repo;



import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blogapp.entities.MyUser;
import com.blogapp.entities.Post;

public interface PostRepository extends JpaRepository<Post, Integer>{
	
	//it will fetch all post from db based on status
	Page<Post> findByStatus(String string, Pageable pageable);
	
	@Query("select p FROM Post p Where p.category.id=:categoryId AND p.status=:status")
	Page<Post> findPostsByCategoryAndStatus(@Param("categoryId") int categoryId, @Param("status") String status, Pageable pageable);
	
	//search implementation
	@Query("select p FROM Post p Where(p.title LIKE concat('%',:search,'%') or p.shortDesc LIKE concat('%',:search,'%')) and p.status = :status")
	Page<Post> searchPost(@Param("search") String search, @Param("status") String status, Pageable pageable);
	
	
	Optional<Post> findByIdAndUrl(int id, String url);
	
	//fetch post based on logged in user
	Page<Post> findByUser(MyUser user, Pageable pageable);
	
	long countByStatus(String status);
	
	long countByUser(MyUser user);

	long countByUserAndStatus(MyUser user, String status);
}
