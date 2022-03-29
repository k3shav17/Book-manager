package com.book.manager.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.Nullable;

import lombok.Data;

@Data
@Document(collection = "Books")
public class Book {

	@Id
	private String id;
	@Indexed(unique = true)
	private String bookName;
	private String authorName;
	@Nullable
	private String country;
	private String language;
	private int noOfPages;
	private int year;
	private Status status;
//	private int pagesCompleted;
	
	// todo : if started how pages have you completed, so no of pages done.

}
