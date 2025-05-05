package org.example.repositories;

import org.example.entities.BookCopy;

import java.sql.SQLException;
import java.util.Collection;

public interface BookCopyRepository extends BasicRepository<BookCopy> {
    Collection<BookCopy> findByUserEmail(String email);

    Collection<BookCopy> findByBookId(Long bookId);

    boolean isBookCopyAvailable(Long id);
}
