package com.example.library.dto;

import com.example.library.model.LoanEntity;

import java.time.LocalDate;

/**
 * DTO for loan responses
 */
public class LoanResponse {
    private String id;
    private String bookId;
    private String bookTitle;
    private String memberId;
    private String memberName;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double fine;
    private boolean active;

    public LoanResponse() {
    }

    public LoanResponse(LoanEntity loan) {
        this.id = loan.getId();
        this.bookId = loan.getBook() != null ? loan.getBook().getId() : null;
        this.bookTitle = loan.getBook() != null ? loan.getBook().getTitle() : null;
        this.memberId = loan.getMember() != null ? loan.getMember().getId() : null;
        this.memberName = loan.getMember() != null ? loan.getMember().getName() : null;
        this.issueDate = loan.getIssueDate();
        this.dueDate = loan.getDueDate();
        this.returnDate = loan.getReturnDate();
        this.fine = loan.getFine();
        this.active = loan.isActive();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
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
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
