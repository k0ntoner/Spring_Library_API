package org.example.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.dtos.customers.CustomerDto;
import org.example.dtos.employees.EmployeeDto;
import org.example.entities.Employee;
import org.example.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@RestController
@RequestMapping("/employees")
@Slf4j
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<?> getEmployees(){
        return ResponseEntity.ok(employeeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable("id") String id){
        return ResponseEntity.ok(employeeService.findById(id));
    }

    @PutMapping
    public ResponseEntity<?> updateEmployee(@RequestBody @Valid EmployeeDto dto){
        return ResponseEntity.ok(employeeService.update(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployeeById(@PathVariable("id") String id){
        employeeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
