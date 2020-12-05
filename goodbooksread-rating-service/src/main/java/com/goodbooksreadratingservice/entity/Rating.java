package com.goodbooksreadratingservice.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Rating {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private int userId;
	private int bookId;
	private int ratingScore;
	
	public Rating() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Rating(int id, int userId, int bookId, int ratingScore) {
		super();
		this.id = id;
		this.userId = userId;
		this.bookId = bookId;
		this.ratingScore = ratingScore;
	}

	public Rating(int userId, int bookId, int ratingScore) {
		super();
		this.userId = userId;
		this.bookId = bookId;
		this.ratingScore = ratingScore;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public int getRatingScore() {
		return ratingScore;
	}

	public void setRatingScore(int ratingScore) {
		this.ratingScore = ratingScore;
	}
	
}
