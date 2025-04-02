package org.example.services.impls;

import lombok.extern.slf4j.Slf4j;
import org.example.dtos.employees.EmployeeDto;
import org.example.entities.Employee;
import org.example.entities.User;
import org.example.exceptions.NotFoundException;
import org.example.exceptions.ServiceException;
import org.example.mappers.EmployeeMapper;
import org.example.repositories.EmployeeRepository;
import org.example.repositories.UserRepository;
import org.example.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {
    private EmployeeRepository employeeRepository;
    private UserRepository userRepository;
    private EmployeeMapper mapper;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, UserRepository userRepository, EmployeeMapper mapper) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public EmployeeDto save(EmployeeDto dto){
        log.debug("Request to save Employee : {}", dto);
        try {
            if (userRepository.findByPhoneNumber(dto.getPhoneNumber()).isPresent()) {
                throw new IllegalArgumentException("User with such phone number: {" + dto.getPhoneNumber() + "}, already exists");
            }
            Employee saved =  employeeRepository.save(mapper.toEntity(dto));
            log.info("Employee saved successfully {}", saved);
            return mapper.toDto(saved);
        }
        catch (Exception e){
            log.error("Error occurred while saving employee: {}", dto, e);
            throw new ServiceException("Could not save employee", e);
        }
    }

    @Override
    public EmployeeDto findById(String id){
        log.debug("Request to find Employee by id : {}", id);
        return mapper.toDto(employeeRepository.findById(id).orElseThrow(() -> new NotFoundException("Employee not found with id: " + id)));

    }

    @Override
    public EmployeeDto update(EmployeeDto dto){
        log.debug("Request to update Employee : {}", dto);
        try {
            EmployeeDto foundEmployee = findById(dto.getId());
            if (foundEmployee.getEmail().equals(dto.getEmail())) {
                Optional<User> user = userRepository.findByPhoneNumber(dto.getPhoneNumber());
                if (user.isEmpty() || user.get().getId().equals(dto.getId())) {
                    Employee updated = employeeRepository.update(mapper.toEntity(dto));
                    log.info("Employee updated successfully {}", updated);
                    return mapper.toDto(updated);
                }
                throw new IllegalArgumentException("User with such phone number: {" + dto.getPhoneNumber() + "}, already exists");
            }
            throw new IllegalArgumentException("While updating employee is promised to change email");
        }
        catch (Exception e){
            log.error("Error occurred while updating employee: {}", dto, e);
            throw new ServiceException("Could not update employee", e);
        }

    }

    @Override
    public void delete(EmployeeDto dto){
        log.debug("Request to delete Employee : {}", dto);
        try {
            employeeRepository.delete(mapper.toEntity(dto));
            log.info("Employee deleted successfully {}", dto);
        }
        catch (Exception e){
            log.error("Error occurred while deleting employee: {}", dto, e);
            throw new ServiceException("Could not delete employee", e);
        }
    }

    @Override
    public void deleteById(String id){
        log.debug("Request to delete Employee by id : {}", id);
        try {
            delete(findById(id));
        }
        catch (Exception e){
            log.error("Error occurred while deleting employee by id: {}", id, e);
            throw new ServiceException("Could not delete employee by id", e);
        }
    }

    @Override
    public Collection<EmployeeDto> findAll(){
        log.debug("Request to find all Employees");
        return mapper.toDtoCollection(employeeRepository.findAll());
    }
}
