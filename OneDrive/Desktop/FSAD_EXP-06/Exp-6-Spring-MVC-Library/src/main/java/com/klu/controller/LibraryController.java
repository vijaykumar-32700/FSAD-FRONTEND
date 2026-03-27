package com.klu.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.klu.model.Book;

@RestController
public class LibraryController {
	
	private List<Book>bookList=new ArrayList<>();
	@GetMapping("/library")
	public String getMessage() {
		return "Welcome to Library";
	}
	
	@GetMapping("/count")
	public int getCount() {
		return bookList.size();
	}
	
	@GetMapping("/price")
	public double getPrice() {
		return 499.99;
	}
	
	@GetMapping("/books")
	public List<Book> getBooks(){
		return bookList; 
	}
	
	@GetMapping("/books/{id}")
	public Book getBook(@PathVariable int id) {
		for(Book b:bookList) {
			if(b.getId()==id)
			{
				return b;
			}
			
		}
		return null;
	}
	
	@GetMapping("/author/{name}")
	public String getAuthor(@PathVariable String name) {
		
		return "books written by "+name;
	}
	
	
	@PostMapping("/addbook")
	public String addBook(@RequestBody Book book)
	{
		bookList.add(book);
		return "book is added ";
	}
	
	@GetMapping("/viewbooks")
	public List<Book> viewBooks(){
		return bookList;
	}
	
}
