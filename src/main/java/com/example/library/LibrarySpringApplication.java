package com.example.library;

import com.example.library.model.Book;
import com.example.library.model.Genre;
import com.example.library.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LibrarySpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibrarySpringApplication.class, args);
	}

	@Bean
	CommandLineRunner loadData(BookRepository bookRepository) {
		return args -> {
			if (bookRepository.count() == 0) {
				bookRepository.save(new Book(
						"Clean Code",
						"Robert C. Martin",
						Genre.TECHNOLOGY,
						3
				));
				bookRepository.save(new Book(
						"Effective Java",
						"Joshua Bloch",
						Genre.TECHNOLOGY,
						2
				));
				bookRepository.save(new Book(
						"The Hobbit",
						"J.R.R. Tolkien",
						Genre.FANTASY,
						5
				));
			}
		};
	}
}