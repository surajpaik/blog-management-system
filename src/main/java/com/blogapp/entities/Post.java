package com.blogapp.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Post {
	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getShortDesc() {
		return shortDesc;
	}


	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public LocalDateTime getCreatedOn() {
		return createdOn;
	}


	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}


	public LocalDateTime getUpdatedOn() {
		return updatedOn;
	}


	public void setUpdatedOn(LocalDateTime updatedOn) {
		this.updatedOn = updatedOn;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getPostImageName() {
		return postImageName;
	}


	public void setPostImageName(String postImageName) {
		this.postImageName = postImageName;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}
	
	public Category getCategory() {
		return category;
	}


	public void setCategory(Category category) {
		this.category = category;
	}


	public List<PostComment> getComment() {
		return comment;
	}


	public void setComment(List<PostComment> comment) {
		this.comment = comment;
	}


	public MyUser getUser() {
		return user;
	}


	public void setUser(MyUser user) {
		this.user = user;
	}



	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false)
	private String title;
	
	
	private String url;
	
	@Column(nullable = false)
	private String shortDesc;
	
	@Lob
	@Column(nullable = false, columnDefinition = "LONGTEXT")
	private String content;
	
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdOn; //when post created
	
	@UpdateTimestamp
	@Column(insertable = false)
	private LocalDateTime updatedOn; // when post updated
	
	@Column(nullable = false)
	private String status; // pending, approved, reject
	
	
	private String postImageName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Category category;
	
	
	//for comments
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	private List<PostComment> comment = new ArrayList<>();
	
	@ManyToOne
	private MyUser user;

}
