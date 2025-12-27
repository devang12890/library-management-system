package com.example.library.fine;

import com.example.library.model.Loan;

import java.time.LocalDate;

// Strategy interface
public interface FineCalculator {
    double calculateFine(Loan loan, LocalDate actualReturnDate);
}