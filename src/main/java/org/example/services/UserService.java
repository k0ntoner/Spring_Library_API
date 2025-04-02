package org.example.services;

import org.example.dtos.users.UserDto;
import org.example.entities.User;

import java.sql.SQLException;
import java.util.Collection;

public interface UserService {
    UserDto findByEmail(String username);

    boolean isPhoneNumberExists(String phoneNumber);

    UserDto findById(String id);

    Collection<UserDto> findAll();

}
