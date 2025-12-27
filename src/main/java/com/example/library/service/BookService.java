package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book create(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + id));
    }

    public Book update(String id, Book updated) {
        Book existing = findById(id);
        existing.setTitle(updated.getTitle());
        existing.setAuthor(updated.getAuthor());
        existing.setGenre(updated.getGenre());
        existing.setTotalCopies(updated.getTotalCopies());
        existing.setAvailableCopies(updated.getAvailableCopies());
        return bookRepository.save(existing);
    }

    public void delete(String id) {
        bookRepository.deleteById(id);
    }

    public List<Book> searchByTitle(String q) {
        return bookRepository.findByTitleContainingIgnoreCase(q);
    }

    public List<Book> searchByAuthor(String q) {
        return bookRepository.findByAuthorContainingIgnoreCase(q);
    }
}