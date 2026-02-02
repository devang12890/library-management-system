package com.example.library.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * JPA Entity for Book Loans
 * Represents a loan transaction between a member and a book
 */
@Entity
@Table(name = "loans")
public class LoanEntity implements Serializable, Identifiable {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @Column(nullable = false)
    private LocalDate issueDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    private LocalDate returnDate;

    private double fine;

    protected LoanEntity() {
    }

    public LoanEntity(Book book, MemberEntity member, LocalDate issueDate, LocalDate dueDate) {
        this.id = UUID.randomUUID().toString();
        this.book = book;
        this.member = member;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
    }

    @PrePersist
    private void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }

    @Override
    public String getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public MemberEntity getMember() {
        return member;
    }

    public void setMember(MemberEntity member) {
        this.member = member;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public double getFine() {
        return fine;
    }

    public void setFine(double fine) {
        this.fine = fine;
    }

    public boolean isActive() {
        return returnDate == null;
    }

    public void markReturned(LocalDate returnDate, double fine) {
        this.returnDate = returnDate;
        this.fine = fine;
    }

    @Override
    public String toString() {
        return String.format("LoanEntity{id='%s', book='%s', member='%s', issue=%s, due=%s, returned=%s, fine=%.2f}",
                id, book != null ? book.getTitle() : "null", 
                member != null ? member.getName() : "null",
                issueDate, dueDate, returnDate != null ? returnDate : "-", fine);
    }
}
