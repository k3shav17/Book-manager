package com.book.manager.dao;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.book.manager.entity.Book;

public interface BookRepository extends MongoRepository<Book, String>{

	Optional<Book> findBookByBookName(String bookName);

//	Optional<Book> findBookById(String id);

}
