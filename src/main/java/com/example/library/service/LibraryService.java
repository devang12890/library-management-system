package com.example.library.service;

import com.example.library.exception.*;
import com.example.library.fine.FineCalculator;
import com.example.library.model.*;
import com.example.library.repository.InMemoryRepository;
import com.example.library.repository.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

// Service layer: coordinates repositories and business rules
public class LibraryService {

    private final Repository<Book> bookRepository = new InMemoryRepository<>();
    private final Repository<Member> memberRepository = new InMemoryRepository<>();
    private final Repository<Loan> loanRepository = new InMemoryRepository<>();

    private static final int REGULAR_MAX_LOANS = 3;
    private static final int PREMIUM_MAX_LOANS = 10;
    private static final int LOAN_DAYS = 14;

    // Book operations
    public Book addBook(String title, String author, Genre genre, int totalCopies) {
        Book book = new Book(title, author, genre, totalCopies);
        return bookRepository.save(book);
    }

    public List<Book> listBooks() {
        return bookRepository.findAll();
    }

    // Method overloading: searchBooks by different criteria

    public List<Book> searchBooks(String title) {
        return bookRepository.findAll().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> searchBooks(String title, String author) {
        return bookRepository.findAll().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(b -> b.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> searchBooks(Genre genre) {
        return bookRepository.findAll().stream()
                .filter(b -> b.getGenre() == genre)
                .collect(Collectors.toList());
    }

    public Book getBookById(String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    // Member operations
    public Member registerMember(String name, String email, MembershipType type) {
        Member member = new Member(name, email, type);
        return memberRepository.save(member);
    }

    public List<Member> listMembers() {
        return memberRepository.findAll();
    }

    public Member getMemberById(String id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));
    }

    // Loan operations
    public List<Loan> listLoans() {
        return loanRepository.findAll();
    }

    public Loan getLoanById(String id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new LoanNotFoundException(id));
    }

    public Loan borrowBook(String memberId, String bookId) {
        Member member = getMemberById(memberId);
        Book book = getBookById(bookId);

        if (!book.isAvailable()) {
            throw new BookNotAvailableException(book.getTitle());
        }

        int activeLoans = (int) loanRepository.findAll().stream()
                .filter(Loan::isActive)
                .filter(loan -> Objects.equals(loan.getMember().getId(), member.getId()))
                .count();

        int maxLoans = member.getMembershipType() == MembershipType.PREMIUM
                ? PREMIUM_MAX_LOANS
                : REGULAR_MAX_LOANS;

        if (activeLoans >= maxLoans) {
            throw new MemberLimitExceededException(
                    "Member " + member.getName() + " has reached maximum loans: " + maxLoans);
        }

        book.borrowCopy();
        bookRepository.save(book); // persist updated state

        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(LOAN_DAYS);
        Loan loan = new Loan(book, member, issueDate, dueDate);
        return loanRepository.save(loan);
    }

    public double returnBook(String loanId) {
        Loan loan = getLoanById(loanId);
        if (!loan.isActive()) {
            return loan.getFine(); // already returned
        }

        Book book = loan.getBook();
        Member member = loan.getMember();

        LocalDate returnDate = LocalDate.now();
        FineCalculator fineCalculator = member.getFineCalculator();
        double fine = fineCalculator.calculateFine(loan, returnDate);

        loan.markReturned(returnDate, fine);
        loanRepository.save(loan);

        book.returnCopy();
        bookRepository.save(book);

        return fine;
    }
}