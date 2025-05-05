package org.example.services.impls;

import lombok.extern.slf4j.Slf4j;
import org.example.dtos.copies.BookCopyDto;
import org.example.entities.BookCopy;
import org.example.enums.Status;
import org.example.exceptions.NotFoundException;
import org.example.exceptions.ServiceException;
import org.example.mappers.BookCopyMapper;
import org.example.repositories.BookCopyRepository;
import org.example.services.BookCopyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Collection;

@Slf4j
@Service
public class BookCopyServiceImpl implements BookCopyService {
    private final BookCopyRepository bookCopyRepository;
    private final BookCopyMapper mapper;

    @Autowired
    public BookCopyServiceImpl(BookCopyRepository bookCopyRepository,  BookCopyMapper bookCopyMapper) {
        this.bookCopyRepository = bookCopyRepository;
        this.mapper = bookCopyMapper;
    }

    @Override
    public Collection<BookCopyDto> findByBookId(Long bookId) {
        log.debug("Request to find BookCopy by bookId {}", bookId);
        return mapper.toDtoCollection(bookCopyRepository.findByBookId(bookId));
    }

    @Override
    public Collection<BookCopyDto> findByEmail(String email){
        log.debug("Request to find BookCopy by email {}", email);
        return mapper.toDtoCollection(bookCopyRepository.findByUserEmail(email));
    }

    @Override
    public boolean isBookCopyAvailable(Long id) {
        log.debug("Request to check if BookCopy available {}", id);
        return bookCopyRepository.isBookCopyAvailable(id);
    }

    @Override
    public BookCopyDto save(BookCopyDto dto){
        log.debug("Request to save BookCopy {}", dto);
        try {
            BookCopy saved = bookCopyRepository.save(mapper.toEntity(dto));
            log.info("BookCopy saved successfully {}", saved);
            return mapper.toDto(saved);
        }
        catch (Exception e) {
            log.error("Error occurred while saving book copy: {}", dto, e);
            throw new ServiceException("Could not save book copy", e);
        }
    }

    @Override
    public BookCopyDto findById(Long id){
        log.debug("Request to find BookCopy with id {}", id);
        return mapper.toDto(bookCopyRepository.findById(id).orElseThrow(() -> new NotFoundException("BookCopy with id " + id + " not found")));
    }

    @Override
    public BookCopyDto update(BookCopyDto dto){
        log.debug("Request to update BookCopy {}", dto);
        try {
            if(findById(dto.getId()) == null) {
                throw new NotFoundException("BookCopy with id " + dto.getId() + " not found");
            }
            BookCopy bookCopy = bookCopyRepository.update(mapper.toEntity(dto));
            log.info("BookCopy updated successfully {}", bookCopy);
            return mapper.toDto(bookCopy);
        }
        catch (Exception e) {
            log.error("Error occurred while updating book copy: {}", dto, e);
            throw new ServiceException("Could not update book copy", e);
        }
    }

    @Override
    public void delete(BookCopyDto dto){
        log.debug("Request to delete BookCopy {}", dto);
        try {
            if (!isBookCopyAvailable(dto.getId())) {
                throw new IllegalArgumentException("BookCopy with id : {" + dto.getId() + "}, is borrowed and cannot be deleted");
            }
            bookCopyRepository.delete(mapper.toEntity(dto));
            log.info("BookCopy deleted successfully {}", dto);
        }
        catch (Exception e) {
            log.error("Error occurred while deleting book copy: {}", dto, e);
            throw new ServiceException("Could not delete book copy", e);
        }
    }

    @Override
    public void deleteById(Long id){
        log.debug("Request to delete BookCopy with id {}", id);
        try {
            delete(findById(id));
        }
        catch (Exception e) {
            log.error("Error occurred while deleting book copy by id: {}", id, e);
            throw new ServiceException("Could not delete book copy by id", e);
        }
    }

    @Override
    public Collection<BookCopyDto> findAll(){
        log.debug("Request to find all BookCopies");
        return mapper.toDtoCollection(bookCopyRepository.findAll());
    }
}
