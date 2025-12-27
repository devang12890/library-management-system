package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody Book book) {
        return bookService.create(book);
    }

    // READ ALL
    @GetMapping
    public List<Book> getAll() {
        return bookService.findAll();
    }

    // READ ONE
    @GetMapping("/{id}")
    public Book getOne(@PathVariable String id) {
        return bookService.findById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Book update(@PathVariable String id, @RequestBody Book book) {
        return bookService.update(id, book);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        bookService.delete(id);
    }

    // SEARCH BY TITLE
    @GetMapping("/search/title")
    public List<Book> searchByTitle(@RequestParam String q) {
        return bookService.searchByTitle(q);
    }

    // SEARCH BY AUTHOR
    @GetMapping("/search/author")
    public List<Book> searchByAuthor(@RequestParam String q) {
        return bookService.searchByAuthor(q);
    }
}