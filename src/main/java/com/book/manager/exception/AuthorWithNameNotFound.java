package com.book.manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthorWithNameNotFound extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	@ExceptionHandler
	public ResponseEntity<Object> authorException(AuthorWithNameNotFound exception) {
		
		return new ResponseEntity<>("No books found with author mentioned.", HttpStatus.NOT_FOUND);
	}

}
