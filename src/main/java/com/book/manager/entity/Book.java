package com.book.manager.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "Books")
public class Book {

	@Id
	private String id;
	@Indexed(unique = true)
	private String bookName;
	private String authorName;
	private int noOfPages;
	private int noOfChapters;

}
