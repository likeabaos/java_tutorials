package com.play.ground.HelloSpringBoot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.play.ground.HelloSpringBoot.exception.IdMissMatchException;
import com.play.ground.HelloSpringBoot.exception.NotFoundException;
import com.play.ground.HelloSpringBoot.persistence.model.Book;
import com.play.ground.HelloSpringBoot.persistence.repo.BookRepo;

@RestController
@RequestMapping("/api/books")
public class BookController {
	@Autowired
	private BookRepo bookRepository;

	@GetMapping
	public Iterable<Book> findAll() {
		return bookRepository.findAll();
	}

	@GetMapping("/title/{bookTitle}")
	public List<Book> findByTitle(@PathVariable String bookTitle) {
		return bookRepository.findByTitle(bookTitle);
	}
	
	@GetMapping("/author/{author}")
	public List<Book> findByAuthor(@PathVariable String author) {
		return bookRepository.findByAuthor(author);
	}

	@GetMapping("/{id}")
	public Book findOne(@PathVariable Long id) {
		return bookRepository.findById(id).orElseThrow(NotFoundException::new);
	}
	
	@GetMapping("/create/{title}/{author}")
	public Book create(@PathVariable String title, @PathVariable String author) {
		Book book = new Book();
		book.setTitle(title);
		book.setAuthor(author);
		bookRepository.save(book);
		return book;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Book create(@RequestBody Book book) {
		return bookRepository.save(book);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		bookRepository.findById(id).orElseThrow(NotFoundException::new);
		bookRepository.deleteById(id);
	}

	@PutMapping("/{id}")
	public Book updateBook(@RequestBody Book book, @PathVariable Long id) {
		if (book.getId() != id) {
			throw new IdMissMatchException();
		}
		bookRepository.findById(id).orElseThrow(NotFoundException::new);
		return bookRepository.save(book);
	}
	
}
