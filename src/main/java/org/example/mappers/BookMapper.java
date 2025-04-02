package org.example.mappers;

import org.example.dtos.books.BookDto;
import org.example.entities.Book;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDto toDto(Book book);

    Book toEntity(BookDto bookDto);

    Collection<BookDto> toDtoCollection(Collection<Book> bookCollection);

    Collection<Book> toEntityCollection(Collection<BookDto> bookDtoCollection);
}
