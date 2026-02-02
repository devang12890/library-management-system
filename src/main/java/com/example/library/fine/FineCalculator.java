package com.example.library.fine;

import com.example.library.model.LoanEntity;

import java.time.LocalDate;

/**
 * Strategy interface for calculating fines
 * Different membership types use different fine calculation strategies
 */
public interface FineCalculator {
    double calculateFine(LoanEntity loan, LocalDate actualReturnDate);
}