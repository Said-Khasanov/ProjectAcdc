package com.javarush.khasanov.repository;


import com.javarush.khasanov.config.SessionFactoryCreator;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class AbstractRepository<T> implements Repository<T> {
    protected final Class<T> entityClass;
    protected final SessionFactoryCreator sessionFactoryCreator;

    @Override
    public T create(T entity) {
        try (Session session = sessionFactoryCreator.getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.save(entity);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
            return entity;
        }
    }

    @Override
    public void update(T entity) {
        try (Session session = sessionFactoryCreator.getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.update(entity);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void delete(T entity) {
        try (Session session = sessionFactoryCreator.getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.remove(entity);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        }
    }

    @Override
    public Optional<T> get(Long id) {
        try (Session session = sessionFactoryCreator.getSession()) {
            return Optional.ofNullable(session.get(entityClass, id));
        }
    }

    @Override
    public Collection<T> getAll() {
        try (Session session = sessionFactoryCreator.getSession()) {
            Query<T> query = session.createQuery("SELECT e FROM %s e".formatted(entityClass.getName()), entityClass);
            return query.list();
        }
    }
}