package com.example.library.repository;

import com.example.library.model.Identifiable;

import java.util.*;

// Simple in-memory implementation using a HashMap
public class InMemoryRepository<T extends Identifiable> implements Repository<T> {

    private final Map<String, T> storage = new HashMap<>();

    @Override
    public T save(T entity) {
        storage.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<T> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(String id) {
        storage.remove(id);
    }
}