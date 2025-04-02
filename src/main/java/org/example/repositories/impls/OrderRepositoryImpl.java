package org.example.repositories.impls;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.entities.*;
import org.example.enums.Status;
import org.example.enums.SubscriptionType;
import org.example.exceptions.RepositoryException;
import org.example.repositories.BookCopyRepository;
import org.example.repositories.OrderRepository;
import org.example.repositories.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.sql.Date;
import java.util.*;

@Slf4j
@Repository
public class OrderRepositoryImpl implements OrderRepository {
    private SessionFactory sessionFactory;

    @Autowired
    public OrderRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public Collection<Order> findByUserId(String user_id){
        log.debug("Fetching orders by user_id: {}", user_id);
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("From Order order where order.user.id=:id").setParameter("id", user_id).getResultList();
        }
        catch (Exception e){
            log.error("Error while trying to fetch Employee by user id {}", user_id, e);
            throw new RepositoryException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Order save(Order entity){
        log.debug("Saving Order: {}", entity);
        try{
            if(entity.getId() != null){
                throw new IllegalArgumentException("Order must not have an id");
            }
            entity = sessionFactory.getCurrentSession().merge(entity);
            return entity;
        }
        catch (Exception e){
            log.error("Error while trying to save Order: {}", entity, e);
            throw new RepositoryException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<Order> findById(Long id){
        log.debug("Fetching Order by id: {}", id);
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Order.class, id));
        }
        catch (Exception e){
            log.error("Error while trying to fetch Order by id: {}", id, e);
            throw new RepositoryException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Order update(Order entity) {
        log.debug("Updating Order: {}", entity);
        try{
            if(entity.getId() == null){
                throw new IllegalArgumentException("Order must have an id");
            }
            entity=sessionFactory.getCurrentSession().merge(entity);
            return entity;
        }
        catch (Exception e){
            log.error("Error while trying to update Order: {}", entity, e);
            throw new RepositoryException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void delete(Order entity) {
        log.debug("Deleting Order: {}", entity);
        try{
            Session session = sessionFactory.getCurrentSession();
            if(!session.contains(entity)){
                entity=session.merge(entity);
            }
            session.remove(entity);
        }
        catch (Exception e){
            log.error("Error while trying to delete Order: {}", entity, e);
            throw new RepositoryException(e.getMessage(), e);
        }
    }

    @Override
    public Collection<Order> findAll() {
        log.debug("Fetching all Orders");
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("From Order order order by order.id asc").getResultList();
        }
        catch (Exception e){
            log.error("Error while trying to fetch all Orders", e);
            throw new RepositoryException(e.getMessage(), e);
        }
    }
}
