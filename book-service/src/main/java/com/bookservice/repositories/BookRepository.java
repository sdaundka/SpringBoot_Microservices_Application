package com.bookservice.repositories;

import org.springframework.data.repository.CrudRepository;
import com.bookservice.entity.Book;

public interface BookRepository extends CrudRepository<Book, Integer> {

	Book findByNameAndAuthor(String name, String author);
}
