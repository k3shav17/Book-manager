package com.book.manager.dao;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.book.manager.entity.Book;

public interface BookRepository extends MongoRepository<Book, String> {
	
	Optional<Book> findBookById(String id);
	
	@Query(value = "{'bookName': {$regex : ?0, $options: 'i'}}")
	Optional<Book> findBookByName(String bookName);	
	
}
