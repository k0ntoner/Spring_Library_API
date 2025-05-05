package org.example.repositories;

import org.example.entities.Customer;
import org.example.entities.Employee;
import org.example.entities.Order;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

public interface EmployeeRepository {
    Employee save(Employee entity);

    Optional<Employee> findById(String id);

    Employee update(Employee entity);

    void delete(Employee entity);

    Collection<Employee> findAll();
}
