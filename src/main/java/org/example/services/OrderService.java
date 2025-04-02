package org.example.services;

import org.example.dtos.orders.OrderDto;
import org.example.entities.Order;

import java.sql.SQLException;
import java.util.Collection;

public interface OrderService extends BasicService<OrderDto> {
    Collection<OrderDto> findByUserId(String user_id);
}
