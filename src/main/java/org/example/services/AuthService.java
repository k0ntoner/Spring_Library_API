package org.example.services;

import org.example.dtos.users.LoginUserDto;
import org.example.dtos.users.UserDto;
import org.example.dtos.users.UserRegistrationDto;

public interface AuthService {
    String login(LoginUserDto loginUserDto);
    UserDto register(UserRegistrationDto userRegistrationDto);
    void logout();
}
