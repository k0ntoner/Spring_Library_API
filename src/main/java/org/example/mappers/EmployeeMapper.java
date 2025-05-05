package org.example.mappers;

import org.example.dtos.customers.CustomerDto;
import org.example.dtos.employees.EmployeeDto;
import org.example.dtos.employees.EmployeeRegistrationDto;
import org.example.entities.Customer;
import org.example.entities.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;

@Mapper(componentModel = "spring",uses = {OrderMapper.class})
public interface EmployeeMapper {
    EmployeeDto toDto(Employee employee);

    EmployeeDto toDto(EmployeeRegistrationDto customer);

    Employee toEntity(EmployeeDto employeeDto);

    Collection<EmployeeDto> toDtoCollection(Collection<Employee> employee);

    Collection<Employee> toEntityCollection(Collection<EmployeeDto> employeeDto);
}
