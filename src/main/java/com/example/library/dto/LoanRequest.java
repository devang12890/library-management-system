package com.example.library.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for creating loans
 */
public class LoanRequest {
    
    @NotBlank(message = "Member ID is required")
    private String memberId;
    
    @NotBlank(message = "Book ID is required")
    private String bookId;

    public LoanRequest() {
    }

    public LoanRequest(String memberId, String bookId) {
        this.memberId = memberId;
        this.bookId = bookId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}
