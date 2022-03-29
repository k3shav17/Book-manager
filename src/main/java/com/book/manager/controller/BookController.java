package com.book.manager.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.book.manager.dao.BookRepository;
import com.book.manager.entity.Book;
import com.book.manager.exception.AuthorWithNameNotFound;
import com.book.manager.exception.BookNotFoundWithNameException;

@RestController
@RequestMapping("/book")
public class BookController {

	@Autowired
	BookRepository bookRepository;

	@Autowired
	MongoTemplate mongoTemplate;

	static String response = "";

	@PostMapping("/add")
	private ResponseEntity<Object> addingBook(@RequestBody Book book) {

		String bookName = book.getBookName();

		bookRepository.findBookByName(bookName).ifPresentOrElse(b -> {
			response = book.getBookName() + " already exists, In your collection.";
		}, () -> {
			response = bookName + " added to the collection.";
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

	@GetMapping("/bookname/{name}")
	private ResponseEntity<Object> getBookByName(@PathVariable String name) {

		Optional<Book> bookDetails = bookRepository.findBookByName(name);

		if (bookDetails.isEmpty()) {
			throw new BookNotFoundWithNameException();
		}

		return new ResponseEntity<>(bookDetails, new HttpHeaders(), HttpStatus.OK);
	}

	@DeleteMapping("/delete/{bookName}")
	private ResponseEntity<Object> deleteBookByName(@PathVariable String bookName) {

		Optional<Book> deletingCollection = bookRepository.findBookByName(bookName);

		if (deletingCollection.isEmpty()) {
			throw new BookNotFoundWithNameException();
		} else
			bookRepository.delete(deletingCollection.get());

		return new ResponseEntity<>("Book has been deleted from the collection. ", new HttpHeaders(), HttpStatus.OK);
	}

	@GetMapping("/sort/name")
	private ResponseEntity<Object> sortingCollectionByBookName() {
		Query query = new Query();
		query.with(Sort.by(Sort.Direction.ASC, "bookName"));

		List<Book> basedOnBookName = mongoTemplate.find(query, Book.class);

		if (basedOnBookName.isEmpty()) {
			throw new BookNotFoundWithNameException();
		}

		return new ResponseEntity<>(basedOnBookName, new HttpHeaders(), HttpStatus.OK);
	}

	@GetMapping("/sort/author")
	private ResponseEntity<Object> sortingCollectionByAuthorName() {
		Query query = new Query();
		query.with(Sort.by(Sort.Direction.ASC, "authorName"));

		List<Book> basedOnAuthorsName = mongoTemplate.find(query, Book.class);

		if (basedOnAuthorsName.isEmpty()) {
			throw new AuthorWithNameNotFound();
		}

		return new ResponseEntity<>(basedOnAuthorsName, new HttpHeaders(), HttpStatus.OK);
	}

	@GetMapping("/sort/year")
	private ResponseEntity<Object> sortingCollectionByYear() {
		Query query = new Query();
		query.with(Sort.by(Sort.Direction.ASC, "year"));

		List<Book> publishedByYear = mongoTemplate.find(query, Book.class);

		return new ResponseEntity<>(publishedByYear, new HttpHeaders(), HttpStatus.OK);
	}

	@GetMapping("/getbyauthor/{authorName}")
	private ResponseEntity<Object> getBooksByAuthors(@PathVariable String authorName) {

		List<Book> authorsBooks = bookRepository.findBookNameByAuthorName(authorName);
		List<String> bookNames = authorsBooks.stream().filter(a -> a.getAuthorName().equals(authorName))
				.map(Book::getBookName).toList();
		return new ResponseEntity<>(bookNames, new HttpHeaders(), HttpStatus.OK);
	}

	@GetMapping("/getbookby/{language}")
	private ResponseEntity<Object> getBookByLanguage(@PathVariable String language) {

		List<Book> bookByLanguage = bookRepository.findBookNameByLanguage(language);

		return new ResponseEntity<>(bookByLanguage, new HttpHeaders(), HttpStatus.OK);
	}

	@GetMapping("/year/{year}")
	private ResponseEntity<Object> getBooksByYear(@PathVariable int year) {
		List<Book> booksByYears = bookRepository.findBookNameByYear(year);

		return new ResponseEntity<>(booksByYears, new HttpHeaders(), HttpStatus.OK);

	}

	@GetMapping("/year/{min}/{max}")
	private ResponseEntity<Object> getBookByYearBetween(@PathVariable int min, @PathVariable int max) {

		List<Book> booksBetweenYears = bookRepository.findBookNameByYearBetween(min, max);

		return new ResponseEntity<>(booksBetweenYears, new HttpHeaders(), HttpStatus.OK);

	}

	@GetMapping("/status/{status}")
	private ResponseEntity<Object> getBooksBasedOnStatus(@PathVariable String status) {

		List<Book> booksByStatus = bookRepository.findBookNameByStatus(status.toUpperCase());

		return new ResponseEntity<>(booksByStatus, new HttpHeaders(), HttpStatus.OK);
	}

	@PutMapping("/update")
	private ResponseEntity<Object> updateBookDetails(@RequestBody Book book) {
		Query query = new Query();
		query.addCriteria(Criteria.where("bookName").is(book.getBookName()));
		Update update = new Update();
		update.set("status", book.getStatus());
		mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().upsert(true).returnNew(false),
				Book.class);
		return new ResponseEntity<>("Book details has been updated. ", new HttpHeaders(), HttpStatus.OK);
	}
}
