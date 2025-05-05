package org.example.repositories.impls;

import lombok.extern.slf4j.Slf4j;

import org.example.entities.*;
import org.example.enums.Status;
import org.example.enums.SubscriptionType;
import org.example.exceptions.RepositoryException;
import org.example.repositories.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;

@Slf4j
@Repository
public class UserRepositoryImpl implements UserRepository {
    private SessionFactory sessionFactory;

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public Optional<User> findByEmail(String email){
        log.debug("Fetching user by email {}", email);
        try (Session session = sessionFactory.openSession()) {
            User user = session.createQuery("From User user where user.email=:email", User.class).setParameter("email", email).uniqueResult();
            return Optional.ofNullable(user);
        }
        catch (Exception e){
            log.error("Error while trying to fetch User by email {}", email, e);
            throw new RepositoryException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<User> findById(String id){
        log.debug("Fetching user by id {}", id);
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            return Optional.ofNullable(user);
        }
        catch (Exception e){
            log.error("Error while trying to fetch User by id {}", id, e);
            throw new RepositoryException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber){
        log.debug("Fetching user by phoneNumber {}", phoneNumber);
        try (Session session = sessionFactory.openSession()) {
            User user = session.createQuery("From User user where user.phoneNumber=:phoneNumber", User.class).setParameter("phoneNumber", phoneNumber).uniqueResult();
            return Optional.ofNullable(user);
        }
        catch (Exception e){
            log.error("Error while trying to fetch User by phoneNumber {}", phoneNumber, e);
            throw new RepositoryException(e.getMessage(), e);
        }
    }

    @Override
    public boolean isUserHasOverdue(String id){
        log.debug("Checking if user has overdue");
        try (Session session = sessionFactory.openSession()) {
            Collection<Order> orders = session.createQuery("From Order order where order.user.id=:id and order.status=:status", Order.class).setParameter("id", id).setParameter("status", Status.OVERDUE).getResultList();
            return !orders.isEmpty();
        }
        catch (Exception e){
            log.error("Error while trying to fetch User by id {}", id, e);
            throw new RepositoryException(e.getMessage(), e);
        }
    }

    @Override
    public Collection<User> findAll() {
        log.debug("Fetching all users");
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("From User", User.class).getResultList();
        }
        catch (Exception e){
            log.error("Error while trying to fetch all Users", e);
            throw new RepositoryException(e.getMessage(), e);
        }
    }
}
