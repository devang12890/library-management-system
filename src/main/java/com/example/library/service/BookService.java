package com.example.library.service;

import com.example.library.dto.BookRequest;
import com.example.library.exception.BookNotFoundException;
import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer for Book operations
 * Handles business logic for book management
 */
@Service
@Transactional
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Create a new book from DTO
     */
    public Book create(BookRequest bookRequest) {
        logger.debug("Creating book: {}", bookRequest.getTitle());
        Book book = new Book(
                bookRequest.getTitle(),
                bookRequest.getAuthor(),
                bookRequest.getGenre(),
                bookRequest.getTotalCopies()
        );
        Book saved = bookRepository.save(book);
        logger.info("Book created successfully with ID: {}", saved.getId());
        return saved;
    }

    /**
     * Find all books with pagination
     */
    @Transactional(readOnly = true)
    public Page<Book> findAll(Pageable pageable) {
        logger.debug("Fetching all books with pagination");
        return bookRepository.findAll(pageable);
    }

    /**
     * Find all books (for backward compatibility)
     */
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        logger.debug("Fetching all books");
        return bookRepository.findAll();
    }

    /**
     * Find a book by ID
     */
    @Transactional(readOnly = true)
    public Book findById(String id) {
        logger.debug("Finding book with ID: {}", id);
        return bookRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Book not found with ID: {}", id);
                    return new BookNotFoundException(id);
                });
    }

    /**
     * Update an existing book
     */
    public Book update(String id, BookRequest bookRequest) {
        logger.debug("Updating book with ID: {}", id);
        Book existing = findById(id);
        existing.setTitle(bookRequest.getTitle());
        existing.setAuthor(bookRequest.getAuthor());
        existing.setGenre(bookRequest.getGenre());
        
        // Update total copies and adjust available copies accordingly
        int oldTotal = existing.getTotalCopies();
        int newTotal = bookRequest.getTotalCopies();
        int difference = newTotal - oldTotal;
        existing.setTotalCopies(newTotal);
        existing.setAvailableCopies(existing.getAvailableCopies() + difference);
        
        Book updated = bookRepository.save(existing);
        logger.info("Book updated successfully with ID: {}", id);
        return updated;
    }

    /**
     * Delete a book by ID
     */
    public void delete(String id) {
        logger.debug("Deleting book with ID: {}", id);
        if (!bookRepository.existsById(id)) {
            logger.warn("Attempted to delete non-existent book with ID: {}", id);
            throw new BookNotFoundException(id);
        }
        bookRepository.deleteById(id);
        logger.info("Book deleted successfully with ID: {}", id);
    }

    /**
     * Search books by title
     */
    @Transactional(readOnly = true)
    public List<Book> searchByTitle(String q) {
        logger.debug("Searching books by title: {}", q);
        return bookRepository.findByTitleContainingIgnoreCase(q);
    }

    /**
     * Search books by author
     */
    @Transactional(readOnly = true)
    public List<Book> searchByAuthor(String q) {
        logger.debug("Searching books by author: {}", q);
        return bookRepository.findByAuthorContainingIgnoreCase(q);
    }
}