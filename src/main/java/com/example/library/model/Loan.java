package com.example.library.model;

import java.time.LocalDate;
import java.util.UUID;

// Composition: Loan "has a" Book and a Member
public class Loan implements Identifiable {
    private final String id;
    private final Book book;
    private final Member member;
    private final LocalDate issueDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;
    private double fine;

    public Loan(Book book, Member member, LocalDate issueDate, LocalDate dueDate) {
        this.id = UUID.randomUUID().toString();
        this.book = book;
        this.member = member;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
    }

    @Override
    public String getId() { return id; }

    public Book getBook() { return book; }

    public Member getMember() { return member; }

    public LocalDate getIssueDate() { return issueDate; }

    public LocalDate getDueDate() { return dueDate; }

    public LocalDate getReturnDate() { return returnDate; }

    public double getFine() { return fine; }

    public boolean isActive() {
        return returnDate == null;
    }

    public void markReturned(LocalDate returnDate, double fine) {
        this.returnDate = returnDate;
        this.fine = fine;
    }

    @Override
    public String toString() {
        return String.format("Loan{id='%s', book='%s', member='%s', issue=%s, due=%s, returned=%s, fine=%.2f}",
                id, book.getTitle(), member.getName(), issueDate, dueDate,
                returnDate != null ? returnDate : "-", fine);
    }
}