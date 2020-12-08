package com.goodbooksreadratingservice.resource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
	
	@Autowired
	private RestTemplate restTemplate;
	
	@GetMapping
	public List<RatingResponse> getRatings() {
		List<RatingResponse> ratingList = new ArrayList<>();
		Iterable<Rating> iterator = ratingRepository.findAll();
		for (Rating rating : iterator) {
			RatingResponse ratingResponse = new RatingResponse();
			ratingResponse.setId(rating.getId());
			HttpHeaders headers = new HttpHeaders();
			ratingResponse.setRatingScore(rating.getRatingScore());
			User ratingUser = new User();
			ratingUser.setUserId(rating.getUserId());
			HttpEntity<User> userRequest = 
				      new HttpEntity<User>(ratingUser, headers);
			ResponseEntity<User> userEntity = restTemplate.exchange("http://USER-SERVICE/user-service/users/"+rating.getUserId(), HttpMethod.GET, userRequest, User.class, 1);;
			if(userEntity != null && userEntity.getBody() != null) {
				User user = userEntity.getBody();
				ratingResponse.setUser(user);
			}
			Book ratedBook = new Book();
			ratedBook.setBookId(rating.getBookId());
			HttpEntity<Book> bookRequest = 
				      new HttpEntity<Book>(ratedBook, headers);
			ResponseEntity<Book> bookEntity = restTemplate.exchange("http://BOOK-SERVICE/book-service/books/"+rating.getBookId(), HttpMethod.GET, bookRequest, Book.class, 1);
			if(bookEntity != null && bookEntity.getBody() != null) {
				Book book = bookEntity.getBody();
				ratingResponse.setBook(book);
			}
			ratingList.add(ratingResponse);
		}
		return ratingList;
	}
	
	@PostMapping(consumes = "application/json")
	public void addRating(@RequestBody RatingRequest ratingRequest) {
		System.out.println("Saving the Rating");
		System.out.println(ratingRequest);
		User ratingUser = ratingRequest.getUser();
		int userId = 0;
		Rating rating = new Rating();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<User> request = new HttpEntity<User>(ratingUser, headers);
		//User user = restTemplate.getForObject("http://USER-SERVICE/user-service/users/"+ratingUser.getName()+"/"+ratingUser.getEmail(), User.class);
		ResponseEntity<User> userEntity = restTemplate
				.exchange("http://USER-SERVICE/user-service/users/"+ratingUser.getName()+"/"+ratingUser.getEmail(), HttpMethod.GET, request, User.class, 1);
		if(userEntity == null || userEntity.getBody() == null) {
			HttpEntity<User> userRequest = 
				      new HttpEntity<User>(ratingUser, headers);
			userEntity = restTemplate
					.postForEntity("http://USER-SERVICE/user-service/users", userRequest, User.class);
			System.out.println("Created the new user : "+userEntity);
			User newUser = userEntity.getBody();
			userId = newUser.getUserId();
		}else {
			User user = userEntity.getBody();
			userId = user.getUserId();
		}
		System.out.println("User id retrieved from the user-service is : "+userId);
		//User user = userRepository.findByNameAndEmail(ratingUser.getName(), ratingUser.getEmail());
		if(userId != 0)
			rating.setUserId(userId);
		
		Book ratingBook = ratingRequest.getBook();
		HttpEntity<Book> ratingBookEntity = new HttpEntity<Book>(ratingBook, headers);
		//Book book = bookRepository.findByNameAndAuthor(bookRating.getName(), bookRating.getAuthor());
		ResponseEntity<Book> bookEntity = restTemplate.exchange("http://BOOK-SERVICE/book-service/books/"+ratingBook.getAuthor()+"/"+ratingBook.getName(), HttpMethod.GET, ratingBookEntity, Book.class, 1);
		int bookId = 0;
		if(bookEntity == null  || bookEntity.getBody() == null) {
			HttpEntity<Book> bookRequest = new HttpEntity<Book>(ratingBook, headers);
			ResponseEntity<Book> newBookEntity = restTemplate.postForEntity("http://BOOK-SERVICE/book-service/books/", bookRequest, Book.class);
			System.out.println("Added the new book : "+newBookEntity);
			Book newBook = newBookEntity.getBody();
			bookId = newBook.getBookId();
		}else {
			Book book = bookEntity.getBody();
			bookId = book.getBookId();
		}
		System.out.println("Book id retrieved from the book-service is : "+bookId);
		if(bookId != 0)
			rating.setBookId(bookId);
		
		rating.setRatingScore(ratingRequest.getRatingScore());
		ratingRepository.save(rating);
	}
}
