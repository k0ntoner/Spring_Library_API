package org.example.repositories;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

public interface BasicRepository<T> {
    T save(T entity);

    Optional<T> findById(Long id);

    T update(T entity);

    void delete(T entity);

    Collection<T> findAll();

}
