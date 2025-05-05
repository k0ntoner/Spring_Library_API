package org.example.services;

import java.sql.SQLException;
import java.util.Collection;

public interface BasicService<T> {
    T save(T dto);

    T findById(Long id);

    T update(T dto);

    void delete(T dto);

    void deleteById(Long id);

    Collection<T> findAll();

}
