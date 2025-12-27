package com.example.library.exception;

public class LoanNotFoundException extends LibraryException {
    public LoanNotFoundException(String loanId) {
        super("Loan not found: " + loanId);
    }
}