package org.example.mappers;

import org.example.dtos.customers.CustomerShortDto;
import org.example.dtos.orders.OrderDto;
import org.example.dtos.users.UserShortDto;
import org.example.entities.Customer;
import org.example.entities.Employee;
import org.example.entities.Order;
import org.example.entities.User;
import org.example.exceptions.NotFoundException;
import org.example.repositories.UserRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", uses = {CustomerShortMapper.class, EmployeeShortMapper.class, BookCopyMapper.class })
public abstract class OrderMapper {

    @Autowired
    protected CustomerShortMapper customerShortMapper;

    @Autowired
    protected EmployeeShortMapper employeeShortMapper;

    @Autowired
    protected BookCopyMapper bookCopyMapper;

    @Autowired
    protected UserRepository userRepository;

    public OrderDto toDto(Order order){
        if(order.getUser() instanceof Customer customer){
            return OrderDto.builder()
                    .id(order.getId())
                    .bookCopyDto(bookCopyMapper.toDto(order.getBookCopy()))
                    .userDto(customerShortMapper.toDto(customer))
                    .expirationDate(order.getExpirationDate())
                    .orderDate(order.getOrderDate())
                    .status(order.getStatus())
                    .subscriptionType(order.getSubscriptionType())
                    .build();
        }
        else if(order.getUser() instanceof Employee employee){
            return OrderDto.builder()
                    .id(order.getId())
                    .bookCopyDto(bookCopyMapper.toDto(order.getBookCopy()))
                    .userDto(employeeShortMapper.toDto(employee))
                    .expirationDate(order.getExpirationDate())
                    .orderDate(order.getOrderDate())
                    .status(order.getStatus())
                    .subscriptionType(order.getSubscriptionType())
                    .build();
        }
        throw  new IllegalArgumentException("Invalid user type");
    }

    public Order toEntity(OrderDto dto){
        return Order.builder()
                .id(dto.getId())
                .bookCopy(bookCopyMapper.toEntity(dto.getBookCopyDto()))
                .user(userRepository.findById(dto.getUserDto().getId()).orElseThrow(() ->new NotFoundException("User with id: \""+dto.getUserDto().getId()+"\" not found")))
                .expirationDate(dto.getExpirationDate())
                .orderDate(dto.getOrderDate())
                .status(dto.getStatus())
                .subscriptionType(dto.getSubscriptionType())
                .build();
    }

    public Collection<OrderDto> toDtoCollection(Collection<Order> orders){
        Collection<OrderDto> dtos = new ArrayList<>();
        for (Order order : orders) {
            dtos.add(toDto(order));
        }
        return dtos;
    }

    public Collection<Order> toEntityCollection(Collection<OrderDto> dtos){
        Collection<Order> orders = new ArrayList<>();
        for (OrderDto dto : dtos) {
            orders.add(toEntity(dto));
        }
        return orders;
    }



}
