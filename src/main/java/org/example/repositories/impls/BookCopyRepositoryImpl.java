package org.example.repositories.impls;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.entities.Book;
import org.example.entities.BookCopy;
import org.example.enums.Status;
import org.example.exceptions.RepositoryException;
import org.example.repositories.BookCopyRepository;
import org.example.repositories.BookRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;

@Slf4j
@Repository
public class BookCopyRepositoryImpl implements BookCopyRepository {
    private SessionFactory sessionFactory;

    @Autowired
    public BookCopyRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<BookCopy> findById(Long id){
        log.debug("Fetching BookCopy with id {}", id);
        try (Session session = sessionFactory.openSession()) {
            BookCopy bookCopy = session.get(BookCopy.class, id);
            return Optional.ofNullable(bookCopy);
        }
        catch (Exception e) {
            log.error("Error while trying to find BookCopy with id {}", id, e);
            throw new RepositoryException(e.getMessage(), e);
        }
    }

    @Override
    public Collection<BookCopy> findByUserEmail(String email){
        log.debug("Fetching BookCopies by user email {}", email);
        try (Session session = sessionFactory.openSession()) {
            Collection<BookCopy> bookCopies = session.createQuery("SELECT copy FROM Order order JOIN order.bookCopy copy WHERE order.user.email = :email", BookCopy.class)
                    .setParameter("email",email)
                    .getResultList();
            return bookCopies;
        }
        catch (Exception e) {
            log.error("Error while trying to find BookCopy by email {}", email, e);
            throw new RepositoryException(e.getMessage(),e);
        }
    }

    @Override
    public Collection<BookCopy> findByBookId(Long bookId){
        log.debug("Fetching BookCopies by book id {}", bookId);
        try (Session session = sessionFactory.openSession()) {
            Collection<BookCopy> bookCopies = session.createQuery("SELECT copy FROM BookCopy copy JOIN copy.book book WHERE book.id = :id", BookCopy.class)
                    .setParameter("id",bookId)
                    .getResultList();
            return bookCopies;
        }
        catch (Exception e) {
            log.error("Error while trying to find BookCopy by book id {}", bookId, e);
            throw new RepositoryException(e.getMessage(),e);
        }

    }

    @Override
    public boolean isBookCopyAvailable(Long id) {
        log.debug("Request to delete BookCopy with id {}", id);
        try (Session session = sessionFactory.openSession()) {
            BookCopy bookCopy = session.get(BookCopy.class, id);
            return bookCopy.getStatus() == Status.AVAILABLE;
        }
    }

    @Override
    @Transactional
    public BookCopy save(BookCopy entity){
        log.debug("Saving BookCopy {}", entity);
        try{
            if(entity.getId()!=null){
                throw new IllegalArgumentException("Book Copy must not have an id");
            }
            sessionFactory.getCurrentSession().persist(entity);
            return entity;
        }
        catch (Exception e){
            log.error("Error while trying to save BookCopy {}", entity, e);
            throw new RepositoryException(e.getMessage(),e);
        }

    }

    @Override
    @Transactional
    public BookCopy update(BookCopy entity) {
        log.debug("Updating BookCopy {}", entity);
        try{
            if(entity.getId()==null){
                throw new IllegalArgumentException("BookCopy must have an id");
            }
            entity = sessionFactory.getCurrentSession().merge(entity);
            return entity;
        }
        catch (Exception e){
            log.error("Error while trying to update BookCopy {}", entity, e);
            throw new RepositoryException(e.getMessage(),e);
        }
    }

    @Override
    @Transactional
    public void delete(BookCopy entity){
        log.debug("Deleting BookCopy {}", entity);
        try{
            Session session = sessionFactory.getCurrentSession();

            if(!session.contains(entity)){
                entity = session.merge(entity);
            }

            session.remove(entity);
        }
        catch (Exception e){
            log.error("Error while trying to delete BookCopy {}", entity, e);
            throw new RepositoryException(e.getMessage(),e);
        }

    }

    @Override
    public Collection<BookCopy> findAll() {
        log.debug("Fetching all BookCopies");
        try (Session session = sessionFactory.openSession()) {
            Collection<BookCopy> bookCopies = session.createQuery("FROM BookCopy").list();
            return bookCopies;
        }
        catch (Exception e) {
            log.error("Error while trying to find all BookCopies", e);
            throw new RepositoryException(e.getMessage(),e);
        }
    }
}
