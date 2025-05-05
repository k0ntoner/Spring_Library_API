package org.example.services;

import org.example.dtos.copies.BookCopyDto;

import java.sql.SQLException;
import java.util.Collection;

public interface BookCopyService extends BasicService<BookCopyDto> {
    Collection<BookCopyDto> findByBookId(Long bookId);

    Collection<BookCopyDto> findByEmail(String email);

    boolean isBookCopyAvailable(Long id);
}
