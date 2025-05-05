package org.example.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.dtos.employees.EmployeeDto;
import org.example.dtos.orders.OrderDto;
import org.example.entities.BookCopy;
import org.example.entities.Order;
import org.example.services.BookCopyService;
import org.example.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<?> getOrders(){
        return ResponseEntity.ok(orderService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable("id") Long id){
        return ResponseEntity.ok(orderService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderDto dto){
        OrderDto saved = orderService.save(dto);
        return ResponseEntity.created(ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(saved.getId())
                        .toUri())
                .body(saved);
    }

    @PutMapping
    public ResponseEntity<?> updateOrder(@RequestBody @Valid OrderDto dto){
        return ResponseEntity.ok(orderService.update(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderById(@PathVariable("id") Long id){
        orderService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
