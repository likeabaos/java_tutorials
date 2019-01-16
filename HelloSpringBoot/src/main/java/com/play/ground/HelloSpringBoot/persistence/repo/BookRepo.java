package com.play.ground.HelloSpringBoot.persistence.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.play.ground.HelloSpringBoot.persistence.model.Book;

public interface BookRepo extends CrudRepository<Book, Long> {
	List<Book> findByTitle(String title);
	List<Book> findByAuthor(String author);
}
