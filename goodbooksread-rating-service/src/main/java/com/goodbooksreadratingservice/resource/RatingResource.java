package com.goodbooksreadratingservice.resource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.goodbooksreadratingservice.entity.Book;
import com.goodbooksreadratingservice.entity.Rating;
import com.goodbooksreadratingservice.entity.RatingRequest;
import com.goodbooksreadratingservice.entity.RatingResponse;
import com.goodbooksreadratingservice.entity.User;
import com.goodbooksreadratingservice.repositories.RatingRepository;



@RestController
@RequestMapping("/ratings")
public class RatingResource {
	
	@Autowired
	private RatingRepository ratingRepository;
	
	@GetMapping
	public List<RatingResponse> getRatings() {
		List<RatingResponse> ratingList = new ArrayList<>();
		Iterable<Rating> iterator = ratingRepository.findAll();
		for (Rating rating : iterator) {
			RatingResponse ratingResponse = new RatingResponse();
			ratingResponse.setId(rating.getId());
			ratingResponse.setRatingScore(rating.getRatingScore());
			User user = new RestTemplate().getForObject("http://localhost:8082/user-service/users/"+rating.getUserId(), User.class);;
			ratingResponse.setUser(user);
			Book book = new RestTemplate().getForObject("http://localhost:8081/book-service/books/"+rating.getBookId(), Book.class);
			ratingResponse.setBook(book);
			ratingList.add(ratingResponse);
		}
		return ratingList;
	}
	
	@PostMapping
	public void addRating(@RequestBody RatingRequest ratingRequest) {
		System.out.println("Saving the Rating");
		System.out.println(ratingRequest);
		User ratingUser = ratingRequest.getUser();
		int userId = 0;
		Rating rating = new Rating();
		User user = new RestTemplate().getForObject("http://localhost:8082/user-service/users/"+ratingUser.getName()+"/"+ratingUser.getEmail(), User.class);
		if(user == null) {
			User newUser = new RestTemplate().postForObject("http://localhost:8082/user-service/users", ratingUser, User.class);
			System.out.println("Created the new user : "+newUser);
			userId = newUser.getUserId();
		}else {
			userId = user.getUserId();
		}
		System.out.println("User id retrieved from the user-service is : "+userId);
		//User user = userRepository.findByNameAndEmail(ratingUser.getName(), ratingUser.getEmail());
		if(userId != 0)
			rating.setUserId(userId);
		
		Book ratingBook = ratingRequest.getBook();
		
		//Book book = bookRepository.findByNameAndAuthor(bookRating.getName(), bookRating.getAuthor());
		Book book = new RestTemplate().getForObject("http://localhost:8081/book-service/books/"+ratingBook.getAuthor()+"/"+ratingBook.getName(), Book.class);
		int bookId = 0;
		if(book == null) {
			Book newBook = new RestTemplate().postForObject("http://localhost:8081/book-service/books/", ratingBook, Book.class);
			System.out.println("Added the new book : "+newBook);
			bookId = newBook.getBookId();
		}else {
			bookId = book.getBookId();
		}
		System.out.println("Book id retrieved from the book-service is : "+bookId);
		if(bookId != 0)
			rating.setBookId(bookId);
		
		rating.setRatingScore(ratingRequest.getRatingScore());
		ratingRepository.save(rating);
	}
}
