package com.book.manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.book.manager.dao.BookRepository;
import com.book.manager.entity.Book;

@RestController
@RequestMapping("/book")
public class BookController {

	@Autowired
	BookRepository bookRepository;

	static String response = "";

	@PostMapping("/add")
	private ResponseEntity<Object> addingBook(@RequestBody Book book) {

		String bookName = book.getBookName();

		bookRepository.findBookByBookName(bookName).ifPresentOrElse(b -> {
			response = "Book with name " + book.getBookName() + " already exists.";
		}, () -> {
			response = "Adding book to the collection.";
			bookRepository.insert(book);
		});

		return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
	}

	@GetMapping("/getallbooks")
	private ResponseEntity<Object> getAllBooks() {

		List<Book> bookCollection = bookRepository.findAll();
		return new ResponseEntity<>(bookCollection, new HttpHeaders(), HttpStatus.OK);
	}

	@GetMapping("/getcount")
	private ResponseEntity<Object> getBooksCount() {

		Long count = bookRepository.count();
		return new ResponseEntity<>("Your book collection contains " + count + " books.", new HttpHeaders(),
				HttpStatus.OK);
	}
	
	// get book by book name
	// ability to update the progress of the book
	
}
