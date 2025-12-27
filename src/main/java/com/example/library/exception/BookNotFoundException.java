package com.example.library.exception;

public class BookNotFoundException extends LibraryException {
    public BookNotFoundException(String bookId) {
        super("Book not found: " + bookId);
    }
}