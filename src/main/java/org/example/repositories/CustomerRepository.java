package org.example.repositories;

import org.example.entities.Customer;
import org.example.entities.Order;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

public interface CustomerRepository {
    Customer save(Customer entity);

    Optional<Customer> findById(String id);

    Customer update(Customer entity);

    void delete(Customer entity);

    Collection<Customer> findAll();

}
