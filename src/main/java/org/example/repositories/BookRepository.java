package org.example.repositories;

import org.example.entities.Book;

import java.sql.SQLException;
import java.util.Collection;

public interface BookRepository extends BasicRepository<Book> {
    boolean isCopiesOfBookBorrowed(Long id);

    boolean isTitleExists(String title);
}
