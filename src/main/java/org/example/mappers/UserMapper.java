package org.example.mappers;

import org.example.dtos.customers.CustomerDto;
import org.example.dtos.customers.CustomerRegistrationDto;
import org.example.dtos.employees.EmployeeDto;
import org.example.dtos.employees.EmployeeRegistrationDto;
import org.example.dtos.employees.EmployeeShortDto;
import org.example.dtos.orders.OrderDto;
import org.example.dtos.users.UserDto;
import org.example.dtos.users.UserRegistrationDto;
import org.example.entities.Customer;
import org.example.entities.Employee;
import org.example.entities.User;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;

@Mapper(componentModel = "spring", uses = {CustomerMapper.class, EmployeeMapper.class})
public abstract class UserMapper {

    @Autowired
    protected CustomerMapper customerMapper;
    @Autowired
    protected EmployeeMapper employeeMapper;

    public User toEntity(UserDto dto) {
        if(dto instanceof CustomerDto customerDto){
            return customerMapper.toEntity(customerDto);
        }
        else if(dto instanceof EmployeeDto employeeDto){
            return employeeMapper.toEntity(employeeDto);
        }
        throw  new IllegalStateException("Invalid user Dto type");
    }

    public UserDto toDto(User user) {
        if(user instanceof Customer customer){
            return customerMapper.toDto(customer);
        }
        else if(user instanceof Employee employee){
            return employeeMapper.toDto(employee);
        }
        throw  new IllegalStateException("Invalid user type");
    }

    public UserDto toDto(UserRegistrationDto userRegistrationDto) {
        if(userRegistrationDto instanceof CustomerRegistrationDto customerRegistrationDto){
            return customerMapper.toDto(customerRegistrationDto);
        }
        else if(userRegistrationDto instanceof EmployeeRegistrationDto employeeRegistrationDto){
            return employeeMapper.toDto(employeeRegistrationDto);
        }
        throw  new IllegalStateException("Invalid user type");
    }

    public Collection<UserDto> toDtoCollection(Collection<User> users) {
        Collection<UserDto> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(toDto(user));
        }
        return dtos;
    }

    public Collection<User>  toEntityCollection(Collection<UserDto> dtos) {
        Collection<User> entities = new ArrayList<>();
        for (UserDto dto : dtos) {
            entities.add(toEntity(dto));
        }
        return entities;
    }
}
