package org.example.services;

import org.example.dtos.employees.EmployeeDto;
import org.example.entities.Employee;
import org.example.repositories.EmployeeRepository;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

public interface EmployeeService {
    EmployeeDto save(EmployeeDto dto);

    EmployeeDto findById(String id);

    EmployeeDto update(EmployeeDto dto);

    void delete(EmployeeDto dto);

    void deleteById(String id);

    Collection<EmployeeDto> findAll();

}
