package com.example.library.fine;

import com.example.library.model.LoanEntity;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Premium fine calculator for premium members
 * Provides 5 days grace period, then charges $0.50 per day late
 */
public class PremiumFineCalculator implements FineCalculator {
    private static final int GRACE_DAYS = 5;
    private static final double DAILY_FINE = 0.5;

    @Override
    public double calculateFine(LoanEntity loan, LocalDate actualReturnDate) {
        long daysLate = ChronoUnit.DAYS.between(loan.getDueDate(), actualReturnDate);
        long chargeableDays = daysLate - GRACE_DAYS;
        if (chargeableDays <= 0) {
            return 0.0;
        }
        return chargeableDays * DAILY_FINE;
    }
}