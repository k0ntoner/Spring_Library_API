package org.example.repositories.impls;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.example.entities.Book;
import org.example.entities.BookCopy;
import org.example.enums.Status;
import org.example.exceptions.RepositoryException;
import org.example.repositories.BookRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class BookRepositoryImpl implements BookRepository {
    private SessionFactory sessionFactory;

    @Autowired
    public BookRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean isCopiesOfBookBorrowed(Long id){
        log.debug("Checking if Book Borrowed by ID {}", id);
        try(Session session = sessionFactory.openSession() ) {
            Collection<BookCopy> bookCopies = session.createQuery("from BookCopy copy where book.id = :id and copy.status=:status").setParameter("id", id).setParameter("status", Status.BORROWED).list();
            if(!bookCopies.isEmpty()){
                return true;
            }
            return false;

        }
        catch (Exception e){
            log.error(e.getMessage());
            throw new RepositoryException(e.getMessage(),e);
        }
    }

    @Override
    public boolean isTitleExists(String title){
        log.debug("Checking if title of book exists {}", title);
        try(Session session = sessionFactory.openSession() ) {
            Book book = session.createQuery("from Book book where book.title = :title ", Book.class).setParameter("title", title).uniqueResult();
            return book != null;

        }
        catch (Exception e){
            log.error(e.getMessage());
            throw new RepositoryException(e.getMessage(),e);
        }
    }

    @Override
    @Transactional
    public Book save(Book entity){
        log.debug("Saving book {}", entity);
        try{
            if(entity.getId() != null){
                throw new IllegalArgumentException("Book must not have an id");
            }
            sessionFactory.getCurrentSession().persist(entity);
            return entity;
        }
        catch (Exception e){
            log.error(e.getMessage());
            throw new RepositoryException(e.getMessage(),e);
        }
    }

    @Override
    public Optional<Book> findById(Long id){
        log.debug("Fetching book by ID {}", id);
        try(Session session = sessionFactory.openSession() ) {
            Book book = session.get(Book.class, id);
            return Optional.ofNullable(book);
        }
        catch (Exception e){
            log.error(e.getMessage());
            throw new RepositoryException(e.getMessage(),e);
        }
    }

    @Override
    @Transactional
    public Book update(Book entity) {
        log.debug("Updating book {}", entity);
        try{
            if(entity.getId() == null){
                throw new IllegalArgumentException("Book must have an id");
            }
            entity = sessionFactory.getCurrentSession().merge(entity);
            return entity;
        }
        catch (Exception e){
            log.error(e.getMessage());
            throw new RepositoryException(e.getMessage(),e);
        }
    }

    @Override
    @Transactional
    public void delete(Book entity){
        log.debug("Deleting book {}", entity);
        try {
            Session session = sessionFactory.getCurrentSession();
            if (!session.contains(entity)) {
                entity = session.find(Book.class, entity.getId());
                if (entity == null) {
                    throw new EntityNotFoundException("Book not found for deletion");
                }
            }
            session.remove(entity);
        }
        catch (Exception e){
            log.error(e.getMessage());
            throw new RepositoryException(e.getMessage(),e);
        }
    }

    @Override
    public Collection<Book> findAll(){
        log.debug("Fetching all books");
        try(Session session = sessionFactory.openSession() ) {
            Collection<Book> books = session.createQuery("from Book").getResultList();
            return books;
        }
        catch (Exception e){
            log.error(e.getMessage());
            throw new RepositoryException(e.getMessage(),e);
        }
    }
}
