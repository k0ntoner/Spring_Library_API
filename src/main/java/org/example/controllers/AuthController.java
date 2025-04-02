package org.example.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.dtos.customers.CustomerRegistrationDto;
import org.example.dtos.employees.EmployeeRegistrationDto;
import org.example.dtos.users.LoginUserDto;
import org.example.dtos.users.UserDto;
import org.example.dtos.users.UserRegistrationDto;
import org.example.services.AuthService;
import org.example.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Map;


@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
        log.debug("AuthController initialized successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginUserDto dto, HttpServletRequest request){
        String token = authService.login(dto);

        request.getSession(true).setAttribute("auth_token", token);
        return ResponseEntity.ok(Map.of(
                "token", token,
                "userId", JwtUtil.extractAuthId(token)
        ));

    }
    @GetMapping("/check")
    public ResponseEntity<?> checkAuth(HttpServletRequest request) {
        Object token = request.getSession(true).getAttribute("auth_token");
        return token != null
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/register/employee")
    public ResponseEntity<?> register(@RequestBody @Valid EmployeeRegistrationDto dto){
        UserDto saved = authService.register(dto);
        return ResponseEntity.created(ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(saved.getId())
                        .toUri())
                .body(saved);
    }
    @PostMapping("/register/customer")
    public ResponseEntity<?> register(@RequestBody @Valid CustomerRegistrationDto dto){
        UserDto saved = authService.register(dto);
        return ResponseEntity.created(ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(saved.getId())
                        .toUri())
                .body(saved);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        request.getSession().invalidate();

        return ResponseEntity.status(302).location(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build()
                .toUri())
                .build();
    }


    
}
