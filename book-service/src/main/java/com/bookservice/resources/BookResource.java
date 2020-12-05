package com.bookservice.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookservice.entity.Book;
import com.bookservice.repositories.BookRepository;



@RestController
@RequestMapping("/books")
public class BookResource {
	
	@Autowired
	private BookRepository bookRepository;
	
	@GetMapping
	public List<Book> getBooks() {
		List<Book> bookList = new ArrayList<>();
		bookRepository.findAll().forEach(bookList::add);
		return bookList;
	}
	
	@PostMapping
	public Book addBook(@RequestBody Book book) {
		System.out.println("Saving the Book");
		System.out.println(book);
		return bookRepository.save(book);
	}
	
	@GetMapping("/{author}/{bookName}")
	public Book getBook(@PathVariable String author, @PathVariable String bookName) {
		System.out.println("Getting the book details for book : "+bookName+" and author : "+author);
		Book book = bookRepository.findByNameAndAuthor(bookName, author);
		if(book == null)
			System.out.println("Book details not found");
		return book;
	}
	
	@GetMapping("/{bookId}")
	public Book getBook(@PathVariable int bookId) {
		Book book = null;
		System.out.println("Getting the book details for book : Id : "+bookId);
		Optional<Book> bookOptionall = bookRepository.findById(bookId);
		if(bookOptionall.isEmpty())
			System.out.println("Book details not found");
		else
			book = bookOptionall.get();
		
		return book;
	}
}
