package org.example.mappers;

import org.example.dtos.books.BookDto;
import org.example.dtos.customers.CustomerDto;
import org.example.dtos.customers.CustomerRegistrationDto;
import org.example.entities.Book;
import org.example.entities.Customer;
import org.example.entities.Order;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;

@Mapper(componentModel = "spring", uses = {OrderMapper.class})
public interface CustomerMapper {
    CustomerDto toDto(Customer customer);

    CustomerDto toDto(CustomerRegistrationDto customer);

    Customer toEntity(CustomerDto customerDto);

    Collection<CustomerDto> toDtoCollection(Collection<Customer> customer);

    Collection<Customer> toEntityCollection(Collection<CustomerDto> customerDto);


}
