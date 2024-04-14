package com.javarush.khasanov.repository;

import com.javarush.khasanov.entity.Identifiable;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractRepository<T extends Identifiable> implements Repository<T> {
    protected final Map<Long, T> map = new ConcurrentHashMap<>();
    public final AtomicLong id = new AtomicLong();

    @Override
    public void create(T entity) {
        entity.setId(id.incrementAndGet());
        map.put(entity.getId(), entity);
    }

    @Override
    public void update(T entity) {
        map.put(entity.getId(), entity);
    }

    @Override
    public void delete(T entity) {
        map.remove(entity.getId());
    }

    @Override
    public Optional<T> get(Long id) {
        id = Objects.requireNonNullElse(id, 0L);
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public Collection<T> getAll() {
        return map.values();
    }
}