package com.example.library.exception;

public class MemberLimitExceededException extends LibraryException {
    public MemberLimitExceededException(String message) {
        super(message);
    }
}