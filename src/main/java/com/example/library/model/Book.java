package com.example.library.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;

// JPA entity
@Entity
@Table(name = "books")
public class Book implements Serializable, Identifiable {

    @Id
    private String id; // removed 'final' so JPA can set it

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    private int totalCopies;
    private int availableCopies;

    // JPA + Jackson need a no-arg constructor
    protected Book() {
        // id will be generated just before persist if null
    }

    public Book(String title, String author, Genre genre, int totalCopies) {
        if (totalCopies <= 0) {
            throw new IllegalArgumentException("totalCopies must be > 0");
        }
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    // If id is null (e.g. created via default constructor + JSON), generate it before insert
    @PrePersist
    private void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }

    @Override
    public String getId() { return id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }

    public void setAuthor(String author) { this.author = author; }

    public Genre getGenre() { return genre; }

    public void setGenre(Genre genre) { this.genre = genre; }

    public int getTotalCopies() { return totalCopies; }

    public void setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; }

    public int getAvailableCopies() { return availableCopies; }

    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }

    public boolean isAvailable() {
        return availableCopies > 0;
    }

    // Encapsulated operations that change internal state
    public void borrowCopy() {
        if (availableCopies <= 0) {
            throw new IllegalStateException("No copies available");
        }
        availableCopies--;
    }

    public void returnCopy() {
        if (availableCopies >= totalCopies) {
            throw new IllegalStateException("All copies are already in library");
        }
        availableCopies++;
    }

    @Override
    public String toString() {
        return String.format("Book{id='%s', title='%s', author='%s', genre=%s, available=%d/%d}",
                id, title, author, genre, availableCopies, totalCopies);
    }
}