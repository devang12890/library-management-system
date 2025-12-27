package com.example.library.model;

// Simple subclass of User
public class Librarian extends User {
    public Librarian(String name, String email) {
        super(name, email);
    }

    @Override
    public String getRoleDescription() {
        return "Librarian (manages library books and members)";
    }
}