package org.example.mappers;

import org.example.dtos.employees.EmployeeShortDto;
import org.example.entities.Employee;
import org.example.entities.Order;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface EmployeeShortMapper {
    EmployeeShortDto toDto(Employee employee);

    Collection<EmployeeShortDto> toDtoCollection(Collection<Employee> employees);
}
