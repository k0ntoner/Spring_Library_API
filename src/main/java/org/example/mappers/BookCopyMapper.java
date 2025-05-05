package org.example.mappers;


import org.example.dtos.copies.BookCopyDto;
import org.example.entities.BookCopy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;

@Mapper(componentModel = "spring", uses = {BookMapper.class})
public interface BookCopyMapper {
    @Mapping(source = "book", target = "bookDto")
    BookCopyDto toDto(BookCopy bookCopy);

    @Mapping(source = "bookDto", target = "book")
    BookCopy toEntity(BookCopyDto bookCopyDto);

    Collection<BookCopyDto> toDtoCollection(Collection<BookCopy> bookCopies);

    Collection<BookCopy> toEntityCollection(Collection<BookCopyDto> bookCopyCollection);
}
