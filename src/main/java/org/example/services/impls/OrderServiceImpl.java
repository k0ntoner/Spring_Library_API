package org.example.services.impls;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.dtos.orders.OrderDto;
import org.example.entities.Order;
import org.example.entities.User;
import org.example.enums.Status;
import org.example.exceptions.NotFoundException;
import org.example.exceptions.ServiceException;
import org.example.mappers.OrderMapper;
import org.example.repositories.BookCopyRepository;
import org.example.repositories.OrderRepository;
import org.example.repositories.UserRepository;
import org.example.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final BookCopyRepository bookCopyRepository;
    private final UserRepository userRepository;
    private final OrderMapper mapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, BookCopyRepository bookCopyRepository, UserRepository userRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.userRepository = userRepository;
        this.mapper = orderMapper;
    }

    @Override
    public Collection<OrderDto> findByUserId(String user_id){
        log.debug("Request to find Orders by user id {}", user_id);
        return mapper.toDtoCollection(orderRepository.findByUserId(user_id));
    }


    private Order checkAndUpdateExpirationStatus(Long id){
        log.debug("Request to check Order expiration by user id {}", id);
        Optional<Order> order = orderRepository.findById(id);

        if (order.isPresent()) {
            if (!order.get().getExpirationDate().isAfter(LocalDate.now()) && order.get().getStatus().equals(Status.BORROWED)) {
                log.info("Order with id:{}, has expired (expired date: " + order.get().getExpirationDate() + ", today: " + LocalDate.now() + "), its status changed to Overdue", id);
                order.get().setStatus(Status.OVERDUE);
                return orderRepository.update(order.get());
            }
            return order.get();
        }
        throw new NotFoundException("Order with id: {" + id + "} not found");

    }

    @Override
    public OrderDto save(OrderDto dto){
        log.debug("Request to save Order {}", dto);
        try {
            if (bookCopyRepository.isBookCopyAvailable(dto.getBookCopyDto().getId())) {
                if (!userRepository.isUserHasOverdue(dto.getUserDto().getId())) {
                    User foundUser = userRepository.findById(dto.getUserDto().getId()).orElseThrow(() -> new NotFoundException("User with id: " + dto.getUserDto().getId() + " not found"));
                    Order saved = orderRepository.save(mapper.toEntity(dto));
                    log.info("Order saved successfully {}", saved);
                    return mapper.toDto(saved);
                }
                throw new IllegalArgumentException("User with id: {" + dto.getUserDto().getId() + "}, has overdue book copy");
            }
            throw new IllegalArgumentException("Copy of book with id: {" + dto.getBookCopyDto().getId() + "}, has already borrowed");
        }
        catch (Exception e){
            log.error("Error while saving Order {}", dto, e);
            throw new ServiceException("Could not save order", e);
        }

    }

    @Override
    public OrderDto findById(Long id){
        log.debug("Request to find Order by id {}", id);
        checkAndUpdateExpirationStatus(id);
        return mapper.toDto(orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found with id: " + id)));
    }

    @Override
    public OrderDto update(OrderDto dto){
        log.debug("Request to update Order {}", dto);
        OrderDto foundOrderDto = findById(dto.getId());
        if (foundOrderDto.getUserDto().getId().equals(dto.getUserDto().getId())) {
            if (foundOrderDto.getBookCopyDto().getId().equals(dto.getBookCopyDto().getId())) {

                Order saved = orderRepository.update(mapper.toEntity(dto));
                log.info("Order updated successfully {}", saved);
                return mapper.toDto(saved);
            }
            throw new IllegalArgumentException("While updating order is promised to change book copy");
        }
        throw new IllegalArgumentException("While updating order is promised to change user");
    }

    @Override
    public void delete(OrderDto dto){
        log.debug("Request to delete Order {}", dto);
        try {
            if (!dto.getStatus().equals(Status.RETURNED)) {
                throw new IllegalArgumentException("Book copy in Order with id: {" + dto.getId() + "} has not been returned");
            }
            orderRepository.delete(mapper.toEntity(dto));
            log.info("Order deleted successfully {}", dto);
        }
        catch (Exception e){
            log.error("Error while deleting Order {}", dto, e);
            throw new ServiceException("Could not delete order", e);
        }
    }

    @Override
    public void deleteById(Long id){
        log.debug("Request to delete Order by id {}", id);
        try {
            delete(findById(id));
        }
        catch (Exception e){
            log.error("Error while deleting Order by id {}", id, e);
            throw new ServiceException("Could not delete order by id", e);
        }
    }

    @Override
    public Collection<OrderDto> findAll(){
        log.debug("Request to find all Orders");
        Collection<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            order.setStatus(checkAndUpdateExpirationStatus(order.getId()).getStatus());
        }
        return mapper.toDtoCollection(orders);
    }
}
