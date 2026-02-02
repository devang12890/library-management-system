package com.example.library.controller;

import com.example.library.dto.MemberRequest;
import com.example.library.dto.MemberResponse;
import com.example.library.service.MemberService;
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

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for Member management operations
 * Provides CRUD operations for library members
 */
@RestController
@RequestMapping("/api/members")
@Tag(name = "Members", description = "Member management APIs")
public class MemberController {

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * Create a new member
     */
    @Operation(summary = "Create a new member", description = "Register a new library member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Member created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or email already exists")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<MemberResponse> create(@Valid @RequestBody MemberRequest memberRequest) {
        logger.info("Creating new member: {}", memberRequest.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MemberResponse(memberService.create(memberRequest)));
    }

    /**
     * Get all members with pagination support
     */
    @Operation(summary = "Get all members", description = "Retrieve all members with optional pagination")
    @GetMapping
    public ResponseEntity<List<MemberResponse>> getAll(Pageable pageable) {
        logger.info("Fetching all members - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        List<MemberResponse> members = memberService.findAll(pageable)
                .stream()
                .map(MemberResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(members);
    }

    /**
     * Get a member by ID
     */
    @Operation(summary = "Get member by ID", description = "Retrieve a specific member by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member found"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getOne(
            @Parameter(description = "Member ID") @PathVariable String id) {
        logger.info("Fetching member with ID: {}", id);
        return ResponseEntity.ok(new MemberResponse(memberService.findById(id)));
    }

    /**
     * Update an existing member
     */
    @Operation(summary = "Update a member", description = "Update an existing member's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member updated successfully"),
            @ApiResponse(responseCode = "404", description = "Member not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MemberResponse> update(
            @Parameter(description = "Member ID") @PathVariable String id,
            @Valid @RequestBody MemberRequest memberRequest) {
        logger.info("Updating member with ID: {}", id);
        return ResponseEntity.ok(new MemberResponse(memberService.update(id, memberRequest)));
    }

    /**
     * Delete a member by ID
     */
    @Operation(summary = "Delete a member", description = "Remove a member from the library")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Member deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(
            @Parameter(description = "Member ID") @PathVariable String id) {
        logger.info("Deleting member with ID: {}", id);
        memberService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
