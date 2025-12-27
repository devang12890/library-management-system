package com.example.library.fine;

import com.example.library.model.Loan;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// Standard members: 1 per day late, no grace period
public class StandardFineCalculator implements FineCalculator {
    private static final double DAILY_FINE = 1.0;

    @Override
    public double calculateFine(Loan loan, LocalDate actualReturnDate) {
        long daysLate = ChronoUnit.DAYS.between(loan.getDueDate(), actualReturnDate);
        if (daysLate <= 0) {
            return 0.0;
        }
        return daysLate * DAILY_FINE;
    }
}