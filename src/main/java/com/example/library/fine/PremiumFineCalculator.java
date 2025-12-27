package com.example.library.fine;

import com.example.library.model.Loan;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// Premium members: 5 days grace, then 0.5 per day
public class PremiumFineCalculator implements FineCalculator {
    private static final int GRACE_DAYS = 5;
    private static final double DAILY_FINE = 0.5;

    @Override
    public double calculateFine(Loan loan, LocalDate actualReturnDate) {
        long daysLate = ChronoUnit.DAYS.between(loan.getDueDate(), actualReturnDate);
        long chargeableDays = daysLate - GRACE_DAYS;
        if (chargeableDays <= 0) {
            return 0.0;
        }
        return chargeableDays * DAILY_FINE;
    }
}