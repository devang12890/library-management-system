package com.example.library.controller;

import com.example.library.dto.LoanRequest;
import com.example.library.dto.LoanResponse;
import com.example.library.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST Controller for Loan management operations
 * Handles book borrowing and returning operations
 */
@RestController
@RequestMapping("/api/loans")
@Tag(name = "Loans", description = "Loan management APIs")
public class LoanController {

    private static final Logger logger = LoggerFactory.getLogger(LoanController.class);
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    /**
     * Borrow a book (create a loan)
     */
    @Operation(summary = "Borrow a book", description = "Create a new loan for a member to borrow a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Loan created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input, book not available, or member limit exceeded"),
            @ApiResponse(responseCode = "404", description = "Book or member not found")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<LoanResponse> borrowBook(@Valid @RequestBody LoanRequest loanRequest) {
        logger.info("Processing loan request for member: {}, book: {}", 
                loanRequest.getMemberId(), loanRequest.getBookId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new LoanResponse(loanService.borrowBook(loanRequest)));
    }

    /**
     * Return a book (complete a loan)
     */
    @Operation(summary = "Return a book", description = "Return a borrowed book and calculate any fines")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book returned successfully"),
            @ApiResponse(responseCode = "404", description = "Loan not found")
    })
    @PostMapping("/{id}/return")
    public ResponseEntity<Map<String, Object>> returnBook(
            @Parameter(description = "Loan ID") @PathVariable String id) {
        logger.info("Processing return for loan: {}", id);
        double fine = loanService.returnBook(id);
        Map<String, Object> response = new HashMap<>();
        response.put("loanId", id);
        response.put("fine", fine);
        response.put("message", fine > 0 
                ? String.format("Book returned. Fine: $%.2f", fine)
                : "Book returned on time. No fine.");
        return ResponseEntity.ok(response);
    }

    /**
     * Get all loans with pagination support
     */
    @Operation(summary = "Get all loans", description = "Retrieve all loans with optional pagination")
    @GetMapping
    public ResponseEntity<List<LoanResponse>> getAll(Pageable pageable) {
        logger.info("Fetching all loans - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        List<LoanResponse> loans = loanService.findAll(pageable)
                .stream()
                .map(LoanResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(loans);
    }

    /**
     * Get a loan by ID
     */
    @Operation(summary = "Get loan by ID", description = "Retrieve a specific loan by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loan found"),
            @ApiResponse(responseCode = "404", description = "Loan not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<LoanResponse> getOne(
            @Parameter(description = "Loan ID") @PathVariable String id) {
        logger.info("Fetching loan with ID: {}", id);
        return ResponseEntity.ok(new LoanResponse(loanService.findById(id)));
    }

    /**
     * Get loans by member ID
     */
    @Operation(summary = "Get loans by member", description = "Retrieve all loans for a specific member")
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<LoanResponse>> getByMember(
            @Parameter(description = "Member ID") @PathVariable String memberId) {
        logger.info("Fetching loans for member: {}", memberId);
        List<LoanResponse> loans = loanService.findByMemberId(memberId)
                .stream()
                .map(LoanResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(loans);
    }

    /**
     * Get active loans by member ID
     */
    @Operation(summary = "Get active loans by member", description = "Retrieve all active (not returned) loans for a specific member")
    @GetMapping("/member/{memberId}/active")
    public ResponseEntity<List<LoanResponse>> getActiveByMember(
            @Parameter(description = "Member ID") @PathVariable String memberId) {
        logger.info("Fetching active loans for member: {}", memberId);
        List<LoanResponse> loans = loanService.findActiveLoansByMemberId(memberId)
                .stream()
                .map(LoanResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(loans);
    }
}
