package com.example.library.exception;

// Base custom exception
public class LibraryException extends RuntimeException {
    public LibraryException(String message) {
        super(message);
    }
}