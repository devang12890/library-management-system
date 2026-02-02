package com.example.library;

import com.example.library.model.Book;
import com.example.library.model.Genre;
import com.example.library.model.MemberEntity;
import com.example.library.model.MembershipType;
import com.example.library.repository.BookRepository;
import com.example.library.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Main Spring Boot Application
 * Library Management System with REST API
 */
@SpringBootApplication
public class LibrarySpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibrarySpringApplication.class, args);
	}

	/**
	 * Load sample data on application startup
	 */
	@Bean
	CommandLineRunner loadData(BookRepository bookRepository, MemberRepository memberRepository) {
		return args -> {
			// Load sample books
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

			// Load sample members
			if (memberRepository.count() == 0) {
				memberRepository.save(new MemberEntity(
						"Alice Johnson",
						"alice.johnson@example.com",
						MembershipType.REGULAR
				));
				memberRepository.save(new MemberEntity(
						"Bob Smith",
						"bob.smith@example.com",
						MembershipType.PREMIUM
				));
			}
		};
	}
}