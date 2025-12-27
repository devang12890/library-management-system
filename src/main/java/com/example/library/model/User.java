package com.example.library.model;

import java.util.UUID;

// Abstract base class (Abstraction, Inheritance)
public abstract class User implements Identifiable {
    private final String id;
    private String name;
    private String email;

    protected User(String name, String email) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
    }

    @Override
    public String getId() {
        return id;
    }

    // Encapsulation with getters/setters
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    // Abstraction & polymorphism: subclasses give role-specific description
    public abstract String getRoleDescription();

    @Override
    public String toString() {
        return String.format("%s{id='%s', name='%s', email='%s'}",
                getClass().getSimpleName(), id, name, email);
    }
}