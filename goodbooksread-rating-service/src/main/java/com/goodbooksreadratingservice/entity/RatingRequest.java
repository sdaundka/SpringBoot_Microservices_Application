package com.goodbooksreadratingservice.entity;

public class RatingRequest {

	private int id;
	private User user;
	private Book book;
	private int ratingScore;
	
	public RatingRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public RatingRequest(int id, User user, Book book, int ratingScore) {
		super();
		this.id = id;
		this.user = user;
		this.book = book;
		this.ratingScore = ratingScore;
	}
	
	public RatingRequest(User user, Book book, int ratingScore) {
		// TODO Auto-generated constructor stub
		//this.id = id;
		this.user = user;
		this.book = book;
		this.ratingScore = ratingScore;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public int getRatingScore() {
		return ratingScore;
	}
	public void setRatingScore(int ratingScore) {
		this.ratingScore = ratingScore;
	}
	


}
