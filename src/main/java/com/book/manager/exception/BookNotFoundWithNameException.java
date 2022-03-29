package com.book.manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BookNotFoundWithNameException extends RuntimeException {

	private static final long serialVersionUID = -6718337692049465852L;
	
	@ExceptionHandler(value = BookNotFoundWithNameException.class)
	public ResponseEntity<Object> exceptionFound(BookNotFoundWithNameException exception) {
	
		return new ResponseEntity<>("Book not found ", HttpStatus.NOT_FOUND);
	}
	
}
