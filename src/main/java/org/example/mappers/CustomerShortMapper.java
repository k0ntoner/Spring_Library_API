package org.example.mappers;

import org.example.dtos.customers.CustomerShortDto;
import org.example.entities.Customer;
import org.example.entities.Order;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface CustomerShortMapper {

    CustomerShortDto toDto(Customer customer);

    Collection<CustomerShortDto> toDtoCollection(Collection<Customer> customers);

}
