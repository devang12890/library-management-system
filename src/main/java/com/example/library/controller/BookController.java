package com.example.library.controller;

import com.example.library.dto.BookRequest;
import com.example.library.dto.BookResponse;
import com.example.library.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for Book management operations
 * Provides CRUD operations and search functionality for books
 */
@RestController
@RequestMapping("/api/books")
@Tag(name = "Books", description = "Book management APIs")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Create a new book
     */
    @Operation(summary = "Create a new book", description = "Add a new book to the library")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BookResponse> create(@Valid @RequestBody BookRequest bookRequest) {
        logger.info("Creating new book: {}", bookRequest.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BookResponse(bookService.create(bookRequest)));
    }

    /**
     * Get all books with pagination support
     */
    @Operation(summary = "Get all books", description = "Retrieve all books with optional pagination")
    @GetMapping
    public ResponseEntity<List<BookResponse>> getAll(Pageable pageable) {
        logger.info("Fetching all books - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        List<BookResponse> books = bookService.findAll(pageable)
                .stream()
                .map(BookResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(books);
    }

    /**
     * Get a book by ID
     */
    @Operation(summary = "Get book by ID", description = "Retrieve a specific book by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book found"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getOne(
            @Parameter(description = "Book ID") @PathVariable String id) {
        logger.info("Fetching book with ID: {}", id);
        return ResponseEntity.ok(new BookResponse(bookService.findById(id)));
    }

    /**
     * Update an existing book
     */
    @Operation(summary = "Update a book", description = "Update an existing book's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> update(
            @Parameter(description = "Book ID") @PathVariable String id,
            @Valid @RequestBody BookRequest bookRequest) {
        logger.info("Updating book with ID: {}", id);
        return ResponseEntity.ok(new BookResponse(bookService.update(id, bookRequest)));
    }

    /**
     * Delete a book by ID
     */
    @Operation(summary = "Delete a book", description = "Remove a book from the library")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(
            @Parameter(description = "Book ID") @PathVariable String id) {
        logger.info("Deleting book with ID: {}", id);
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Search books by title
     */
    @Operation(summary = "Search books by title", description = "Find books matching the title (case-insensitive)")
    @GetMapping("/search/title")
    public ResponseEntity<List<BookResponse>> searchByTitle(
            @Parameter(description = "Search query") @RequestParam String q) {
        logger.info("Searching books by title: {}", q);
        List<BookResponse> books = bookService.searchByTitle(q)
                .stream()
                .map(BookResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(books);
    }

    /**
     * Search books by author
     */
    @Operation(summary = "Search books by author", description = "Find books by author name (case-insensitive)")
    @GetMapping("/search/author")
    public ResponseEntity<List<BookResponse>> searchByAuthor(
            @Parameter(description = "Search query") @RequestParam String q) {
        logger.info("Searching books by author: {}", q);
        List<BookResponse> books = bookService.searchByAuthor(q)
                .stream()
                .map(BookResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(books);
    }
}