package com.javarush.khasanov.repository;

import java.util.Collection;
import java.util.Optional;

public interface Repository<T> {
    void create(T entity);

    void update(T entity);

    void delete(T entity);

    Optional<T> get(Long id);

    Collection<T> getAll();
}