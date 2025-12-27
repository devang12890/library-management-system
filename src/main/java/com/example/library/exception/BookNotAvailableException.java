package com.example.library.exception;

public class BookNotAvailableException extends LibraryException {
    public BookNotAvailableException(String bookTitle) {
        super("Book not available: " + bookTitle);
    }
}