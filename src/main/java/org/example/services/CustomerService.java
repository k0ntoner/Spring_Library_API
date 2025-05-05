package org.example.services;

import org.example.dtos.customers.CustomerDto;
import org.example.entities.Customer;
import org.example.entities.Order;

import java.sql.SQLException;
import java.util.Collection;

public interface CustomerService {
    CustomerDto save(CustomerDto dto);

    CustomerDto findById(String id);

    CustomerDto update(CustomerDto dto);

    void delete(CustomerDto dto);

    void deleteById(String id);

    Collection<CustomerDto> findAll();

}
