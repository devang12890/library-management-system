package com.example.library.service;

import com.example.library.dto.LoanRequest;
import com.example.library.exception.*;
import com.example.library.fine.FineCalculator;
import com.example.library.fine.PremiumFineCalculator;
import com.example.library.fine.StandardFineCalculator;
import com.example.library.model.Book;
import com.example.library.model.LoanEntity;
import com.example.library.model.MemberEntity;
import com.example.library.model.MembershipType;
import com.example.library.repository.BookRepository;
import com.example.library.repository.LoanRepository;
import com.example.library.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Service layer for Loan operations
 * Handles business logic for book loans and returns
 */
@Service
@Transactional
public class LoanService {

    private static final Logger logger = LoggerFactory.getLogger(LoanService.class);
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    private static final int REGULAR_MAX_LOANS = 3;
    private static final int PREMIUM_MAX_LOANS = 10;
    private static final int LOAN_DAYS = 14;

    public LoanService(LoanRepository loanRepository, BookRepository bookRepository, 
                      MemberRepository memberRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    /**
     * Borrow a book (create a loan)
     */
    public LoanEntity borrowBook(LoanRequest loanRequest) {
        logger.debug("Processing loan request for member: {}, book: {}", 
                loanRequest.getMemberId(), loanRequest.getBookId());
        
        MemberEntity member = memberRepository.findById(loanRequest.getMemberId())
                .orElseThrow(() -> {
                    logger.warn("Member not found: {}", loanRequest.getMemberId());
                    return new MemberNotFoundException(loanRequest.getMemberId());
                });
        
        Book book = bookRepository.findById(loanRequest.getBookId())
                .orElseThrow(() -> {
                    logger.warn("Book not found: {}", loanRequest.getBookId());
                    return new BookNotFoundException(loanRequest.getBookId());
                });

        if (!book.isAvailable()) {
            logger.warn("Book not available: {}", book.getTitle());
            throw new BookNotAvailableException(book.getTitle());
        }

        // Check active loans count
        List<LoanEntity> activeLoans = loanRepository.findByMemberIdAndReturnDateIsNull(member.getId());
        int maxLoans = member.getMembershipType() == MembershipType.PREMIUM
                ? PREMIUM_MAX_LOANS
                : REGULAR_MAX_LOANS;

        if (activeLoans.size() >= maxLoans) {
            logger.warn("Member {} has reached maximum loans: {}", member.getName(), maxLoans);
            throw new MemberLimitExceededException(
                    "Member " + member.getName() + " has reached maximum loans: " + maxLoans);
        }

        // Borrow the book
        book.borrowCopy();
        bookRepository.save(book);

        // Create loan
        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(LOAN_DAYS);
        LoanEntity loan = new LoanEntity(book, member, issueDate, dueDate);
        LoanEntity saved = loanRepository.save(loan);
        
        logger.info("Loan created successfully with ID: {}", saved.getId());
        return saved;
    }

    /**
     * Return a book (complete a loan)
     */
    public double returnBook(String loanId) {
        logger.debug("Processing return for loan: {}", loanId);
        
        LoanEntity loan = loanRepository.findById(loanId)
                .orElseThrow(() -> {
                    logger.warn("Loan not found: {}", loanId);
                    return new LoanNotFoundException(loanId);
                });
        
        if (!loan.isActive()) {
            logger.info("Loan {} already returned, fine: {}", loanId, loan.getFine());
            return loan.getFine();
        }

        Book book = loan.getBook();
        MemberEntity member = loan.getMember();

        LocalDate returnDate = LocalDate.now();
        FineCalculator fineCalculator = getFineCalculator(member.getMembershipType());
        double fine = calculateFine(loan, returnDate, fineCalculator);

        loan.markReturned(returnDate, fine);
        loanRepository.save(loan);

        book.returnCopy();
        bookRepository.save(book);

        logger.info("Book returned successfully, fine: {}", fine);
        return fine;
    }

    /**
     * Calculate fine for a loan
     */
    private double calculateFine(LoanEntity loan, LocalDate returnDate, FineCalculator calculator) {
        return calculator.calculateFine(loan, returnDate);
    }

    /**
     * Get appropriate fine calculator based on membership type
     */
    private FineCalculator getFineCalculator(MembershipType type) {
        return type == MembershipType.PREMIUM 
                ? new PremiumFineCalculator() 
                : new StandardFineCalculator();
    }

    /**
     * Find all loans with pagination
     */
    @Transactional(readOnly = true)
    public Page<LoanEntity> findAll(Pageable pageable) {
        logger.debug("Fetching all loans with pagination");
        return loanRepository.findAll(pageable);
    }

    /**
     * Find all loans
     */
    @Transactional(readOnly = true)
    public List<LoanEntity> findAll() {
        logger.debug("Fetching all loans");
        return loanRepository.findAll();
    }

    /**
     * Find a loan by ID
     */
    @Transactional(readOnly = true)
    public LoanEntity findById(String id) {
        logger.debug("Finding loan with ID: {}", id);
        return loanRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Loan not found with ID: {}", id);
                    return new LoanNotFoundException(id);
                });
    }

    /**
     * Find loans by member ID
     */
    @Transactional(readOnly = true)
    public List<LoanEntity> findByMemberId(String memberId) {
        logger.debug("Finding loans for member: {}", memberId);
        return loanRepository.findByMemberId(memberId);
    }

    /**
     * Find active loans by member ID
     */
    @Transactional(readOnly = true)
    public List<LoanEntity> findActiveLoansByMemberId(String memberId) {
        logger.debug("Finding active loans for member: {}", memberId);
        return loanRepository.findByMemberIdAndReturnDateIsNull(memberId);
    }
}
