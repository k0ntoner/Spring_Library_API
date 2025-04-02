package org.example.services.impls;

import lombok.extern.slf4j.Slf4j;
import org.example.dtos.books.BookDto;
import org.example.entities.Book;
import org.example.exceptions.NotFoundException;
import org.example.exceptions.ServiceException;
import org.example.mappers.BookMapper;
import org.example.repositories.BookRepository;
import org.example.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper mapper;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, BookMapper mapper) {
        this.bookRepository = bookRepository;
        this.mapper = mapper;
    }

    @Override
    public BookDto save(BookDto dto){
        log.info("Request to save Book {}", dto);
        try {
            if (bookRepository.isTitleExists(dto.getTitle())) {
                throw new IllegalArgumentException("Title: {" + dto.getTitle() + "} already exists");
            }
            Book saved = bookRepository.save(mapper.toEntity(dto));
            log.info("Book saved successfully {}", saved);
            return mapper.toDto(saved);
        }
        catch (Exception e){
            log.error("Error occurred while saving book: {}", dto, e);
            throw new ServiceException("Could not save book", e);
        }
    }

    @Override
    public BookDto findById(Long id){
        log.info("Request to find Book by id {}", id);
        return mapper.toDto(bookRepository.findById(id).orElseThrow(() -> new NotFoundException("Book not found with id: " + id)));

    }

    @Override
    public BookDto update(BookDto dto){
        log.info("Request to update Book {}", dto);
        try {
            if(findById(dto.getId()) == null) {
                throw new NotFoundException("BookCopy with id " + dto.getId() + " not found");
            }
            if (bookRepository.isTitleExists(dto.getTitle())) {
                throw new IllegalArgumentException("Title: {" + dto.getTitle() + "} already exists");
            }
            Book updated = bookRepository.update(mapper.toEntity(dto));
            log.info("Book updated successfully {}", updated);
            return mapper.toDto(updated);
        }
        catch (Exception e){
            log.error("Error occurred while updating book: {}", dto, e);
            throw new ServiceException("Could not update book", e);
        }
    }

    @Override
    public void delete(BookDto dto){
        log.info("Request to delete Book {}", dto);
        try {
            if (bookRepository.isCopiesOfBookBorrowed(dto.getId())) {
                throw new IllegalStateException("Copies of the book with id: {" + dto.getId() + "} is borrowed!");
            }
            bookRepository.delete(mapper.toEntity(dto));
            log.info("Book deleted successfully {}", dto);
        }
        catch (Exception e){
            log.error("Error occurred while deleting book: {}", dto, e);
            throw new ServiceException("Could not delete book", e);
        }
    }

    @Override
    public void deleteById(Long id){
        log.info("Request to delete Book by id {}", id);
        try {
            delete(findById(id));
        }
        catch (Exception e){
            log.error("Error occurred while deleting book by id: {}", id, e);
            throw new ServiceException("Could not delete book", e);
        }
    }

    @Override
    public Collection<BookDto> findAll(){
        log.info("Request to find all Books");
        return mapper.toDtoCollection(bookRepository.findAll());
    }
}
