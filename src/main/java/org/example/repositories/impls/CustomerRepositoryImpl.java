package org.example.repositories.impls;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import org.example.entities.*;
import org.example.enums.Status;
import org.example.enums.SubscriptionType;
import org.example.exceptions.RepositoryException;
import org.example.repositories.CustomerRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.sql.Date;
import java.util.*;

@Slf4j
@Repository
public class CustomerRepositoryImpl implements CustomerRepository {
    private SessionFactory sessionFactory;

    @Autowired
    public CustomerRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional
    public Customer save(Customer entity){
        log.debug("Saving customer {}", entity);
        try{
            sessionFactory.getCurrentSession().persist(entity);
            return entity;
        }
        catch (Exception e){
            log.error("Error while trying to save customer {}", entity, e);
            throw new RepositoryException(e.getMessage(),e);
        }
    }

    @Override
    public Optional<Customer> findById(String id){
        log.debug("Fetching customer with id {}", id);
        try(Session session = sessionFactory.openSession()){
            Customer entity = session.get(Customer.class, id);
            return Optional.ofNullable(entity);
        }
        catch (Exception e){
            log.error("Error while trying to fetch customer {}", id, e);
            throw new RepositoryException(e.getMessage(),e);
        }
    }

    @Override
    @Transactional
    public Customer update(Customer entity){
        log.debug("Updating customer {}", entity);
        try{
            if(entity.getId()==null){
                throw new IllegalArgumentException("Customer must have an id");
            }
            entity=sessionFactory.getCurrentSession().merge(entity);
            return entity;
        }
        catch (Exception e){
            log.error("Error while trying to update customer {}", entity, e);
            throw new RepositoryException(e.getMessage(),e);
        }
    }

    @Override
    @Transactional
    public void delete(Customer entity){
        log.debug("Deleting customer {}", entity);
        try{
            Session session = sessionFactory.getCurrentSession();
            if(!session.contains(entity)){
                entity=session.merge(entity);
            }
            session.remove(entity);
        }
        catch (Exception e){
            log.error("Error while trying to delete customer {}", entity, e);
            throw new RepositoryException(e.getMessage(),e);
        }
    }

    @Override
    public Collection<Customer> findAll(){
        log.debug("Fetching all customers");
        try(Session session = sessionFactory.openSession()){
            Collection<Customer> customers = session.createQuery("from Customer").getResultList();
            return customers;
        }
        catch (Exception e){
            log.error("Error while trying to fetch all customers", e);
            throw new RepositoryException(e.getMessage(),e);
        }
    }

}
