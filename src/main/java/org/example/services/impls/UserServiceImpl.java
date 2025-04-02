package org.example.services.impls;

import lombok.extern.slf4j.Slf4j;
import org.example.dtos.users.UserDto;
import org.example.entities.User;
import org.example.exceptions.NotFoundException;
import org.example.mappers.UserMapper;
import org.example.repositories.OrderRepository;
import org.example.repositories.UserRepository;
import org.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final UserMapper mapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, OrderRepository orderRepository, UserMapper mapper) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    @Override
    public UserDto findByEmail(String email){
        log.info("Request to find User by email: {}", email);
        return mapper.toDto(userRepository.findByEmail(email).orElseThrow( () -> new NotFoundException("User with email "+email+" not found")));

    }


    @Override
    public boolean isPhoneNumberExists(String phoneNumber){
        log.info("Request to check if Phone Number exists for user: {}", phoneNumber);
        return userRepository.findByPhoneNumber(phoneNumber).isPresent();

    }

    @Override
    public UserDto findById(String id){
        log.info("Request to find User by id: {}", id);
        return mapper.toDto(userRepository.findById(id).orElseThrow(() ->  new NotFoundException("User with id "+id+" not found")));
    }

    @Override
    public Collection<UserDto> findAll(){
        log.info("Request to find all Users");
        return mapper.toDtoCollection(userRepository.findAll());
    }
}
