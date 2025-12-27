package com.example.library;

import com.example.library.exception.LibraryException;
import com.example.library.model.*;
import com.example.library.service.LibraryService;
import com.example.library.util.BookCsvExporter;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class LibraryApplication {

    private final LibraryService libraryService = new LibraryService();
    private final BookCsvExporter exporter = new BookCsvExporter();
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new LibraryApplication().run();
    }

    private void run() {
        seedSampleData();

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1":
                        addBook();
                        break;
                    case "2":
                        listBooks();
                        break;
                    case "3":
                        searchBooks();
                        break;
                    case "4":
                        registerMember();
                        break;
                    case "5":
                        listMembers();
                        break;
                    case "6":
                        borrowBook();
                        break;
                    case "7":
                        returnBook();
                        break;
                    case "8":
                        listLoans();
                        break;
                    case "9":
                        exportBooks();
                        break;
                    case "0":
                        running = false;
                        System.out.println("Exiting. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice, try again.");
                }
            } catch (LibraryException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
                e.printStackTrace();
            }
            System.out.println();
        }
    }

    private void printMenu() {
        System.out.println("===== Library Management System =====");
        System.out.println("1. Add book");
        System.out.println("2. List books");
        System.out.println("3. Search books");
        System.out.println("4. Register member");
        System.out.println("5. List members");
        System.out.println("6. Borrow book");
        System.out.println("7. Return book");
        System.out.println("8. List loans");
        System.out.println("9. Export books to CSV");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private void seedSampleData() {
        libraryService.addBook("Clean Code", "Robert C. Martin", Genre.TECHNOLOGY, 3);
        libraryService.addBook("Effective Java", "Joshua Bloch", Genre.TECHNOLOGY, 2);
        libraryService.addBook("The Hobbit", "J.R.R. Tolkien", Genre.FANTASY, 5);

        libraryService.registerMember("Alice", "alice@example.com", MembershipType.REGULAR);
        libraryService.registerMember("Bob", "bob@example.com", MembershipType.PREMIUM);
    }

    private void addBook() {
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();

        System.out.print("Author: ");
        String author = scanner.nextLine().trim();

        System.out.println("Genre (FICTION, NON_FICTION, SCIENCE, HISTORY, TECHNOLOGY, BIOGRAPHY, FANTASY, OTHER): ");
        String genreStr = scanner.nextLine().trim().toUpperCase();
        Genre genre;
        try {
            genre = Genre.valueOf(genreStr);
        } catch (IllegalArgumentException e) {
            genre = Genre.OTHER;
        }

        System.out.print("Total copies: ");
        int copies = Integer.parseInt(scanner.nextLine().trim());

        Book book = libraryService.addBook(title, author, genre, copies);
        System.out.println("Added: " + book);
    }

    private void listBooks() {
        List<Book> books = libraryService.listBooks();
        if (books.isEmpty()) {
            System.out.println("No books.");
            return;
        }
        System.out.println("Books:");
        books.forEach(book -> System.out.println(" - " + book));
    }

    private void searchBooks() {
        System.out.println("Search by: 1) Title  2) Title & Author  3) Genre");
        String option = scanner.nextLine().trim();

        List<Book> results;
        switch (option) {
            case "1":
                System.out.print("Title contains: ");
                String title = scanner.nextLine().trim();
                results = libraryService.searchBooks(title);
                break;
            case "2":
                System.out.print("Title contains: ");
                String t = scanner.nextLine().trim();
                System.out.print("Author contains: ");
                String a = scanner.nextLine().trim();
                results = libraryService.searchBooks(t, a);
                break;
            case "3":
                System.out.println("Genre (FICTION, NON_FICTION, SCIENCE, HISTORY, TECHNOLOGY, BIOGRAPHY, FANTASY, OTHER): ");
                String genreStr = scanner.nextLine().trim().toUpperCase();
                Genre genre;
                try {
                    genre = Genre.valueOf(genreStr);
                } catch (IllegalArgumentException e) {
                    genre = Genre.OTHER;
                }
                results = libraryService.searchBooks(genre);
                break;
            default:
                System.out.println("Invalid option.");
                return;
        }

        if (results.isEmpty()) {
            System.out.println("No matching books found.");
        } else {
            System.out.println("Results:");
            results.forEach(b -> System.out.println(" - " + b));
        }
    }

    private void registerMember() {
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Membership type (REGULAR/PREMIUM): ");
        String typeStr = scanner.nextLine().trim().toUpperCase();
        MembershipType type = typeStr.equals("PREMIUM") ? MembershipType.PREMIUM : MembershipType.REGULAR;

        Member member = libraryService.registerMember(name, email, type);
        System.out.println("Registered: " + member + " | " + member.getRoleDescription());
    }

    private void listMembers() {
        List<Member> members = libraryService.listMembers();
        if (members.isEmpty()) {
            System.out.println("No members.");
            return;
        }
        System.out.println("Members:");
        members.forEach(m -> System.out.println(" - " + m + " | " + m.getRoleDescription()));
    }

    private void borrowBook() {
        listMembers();
        System.out.print("Enter member ID: ");
        String memberId = scanner.nextLine().trim();

        listBooks();
        System.out.print("Enter book ID: ");
        String bookId = scanner.nextLine().trim();

        Loan loan = libraryService.borrowBook(memberId, bookId);
        System.out.println("Loan created: " + loan);
    }

    private void returnBook() {
        listLoans();
        System.out.print("Enter loan ID: ");
        String loanId = scanner.nextLine().trim();

        double fine = libraryService.returnBook(loanId);
        System.out.printf("Book returned. Fine: %.2f%n", fine);
    }

    private void listLoans() {
        List<Loan> loans = libraryService.listLoans();
        if (loans.isEmpty()) {
            System.out.println("No loans.");
            return;
        }
        System.out.println("Loans:");
        loans.forEach(l -> System.out.println(" - " + l));
    }

    private void exportBooks() {
        System.out.print("Enter file path for CSV (e.g., books.csv): ");
        String path = scanner.nextLine().trim();
        try {
            exporter.exportToCsv(libraryService.listBooks(), path);
            System.out.println("Books exported to " + path);
        } catch (IOException e) {
            System.out.println("Failed to export: " + e.getMessage());
        }
    }
}