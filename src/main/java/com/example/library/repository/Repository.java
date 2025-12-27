package com.example.library.repository;

import com.example.library.model.Identifiable;

import java.util.List;
import java.util.Optional;

// Generic repository interface
public interface Repository<T extends Identifiable> {
    T save(T entity);
    Optional<T> findById(String id);
    List<T> findAll();
    void deleteById(String id);
}