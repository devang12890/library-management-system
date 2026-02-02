package com.example.library.service;

import com.example.library.dto.MemberRequest;
import com.example.library.exception.MemberNotFoundException;
import com.example.library.model.MemberEntity;
import com.example.library.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer for Member operations
 * Handles business logic for member management
 */
@Service
@Transactional
public class MemberService {

    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * Create a new member
     */
    public MemberEntity create(MemberRequest memberRequest) {
        logger.debug("Creating member: {}", memberRequest.getEmail());
        
        // Check if email already exists
        if (memberRepository.findByEmail(memberRequest.getEmail()).isPresent()) {
            logger.warn("Attempted to create member with existing email: {}", memberRequest.getEmail());
            throw new IllegalArgumentException("Member with email " + memberRequest.getEmail() + " already exists");
        }
        
        MemberEntity member = new MemberEntity(
                memberRequest.getName(),
                memberRequest.getEmail(),
                memberRequest.getMembershipType()
        );
        MemberEntity saved = memberRepository.save(member);
        logger.info("Member created successfully with ID: {}", saved.getId());
        return saved;
    }

    /**
     * Find all members with pagination
     */
    @Transactional(readOnly = true)
    public Page<MemberEntity> findAll(Pageable pageable) {
        logger.debug("Fetching all members with pagination");
        return memberRepository.findAll(pageable);
    }

    /**
     * Find all members
     */
    @Transactional(readOnly = true)
    public List<MemberEntity> findAll() {
        logger.debug("Fetching all members");
        return memberRepository.findAll();
    }

    /**
     * Find a member by ID
     */
    @Transactional(readOnly = true)
    public MemberEntity findById(String id) {
        logger.debug("Finding member with ID: {}", id);
        return memberRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Member not found with ID: {}", id);
                    return new MemberNotFoundException(id);
                });
    }

    /**
     * Update an existing member
     */
    public MemberEntity update(String id, MemberRequest memberRequest) {
        logger.debug("Updating member with ID: {}", id);
        MemberEntity existing = findById(id);
        
        // Check if email is being changed and if new email already exists
        if (!existing.getEmail().equals(memberRequest.getEmail())) {
            if (memberRepository.findByEmail(memberRequest.getEmail()).isPresent()) {
                logger.warn("Attempted to update member with existing email: {}", memberRequest.getEmail());
                throw new IllegalArgumentException("Member with email " + memberRequest.getEmail() + " already exists");
            }
        }
        
        existing.setName(memberRequest.getName());
        existing.setEmail(memberRequest.getEmail());
        existing.setMembershipType(memberRequest.getMembershipType());
        
        MemberEntity updated = memberRepository.save(existing);
        logger.info("Member updated successfully with ID: {}", id);
        return updated;
    }

    /**
     * Delete a member by ID
     */
    public void delete(String id) {
        logger.debug("Deleting member with ID: {}", id);
        if (!memberRepository.existsById(id)) {
            logger.warn("Attempted to delete non-existent member with ID: {}", id);
            throw new MemberNotFoundException(id);
        }
        memberRepository.deleteById(id);
        logger.info("Member deleted successfully with ID: {}", id);
    }
}
