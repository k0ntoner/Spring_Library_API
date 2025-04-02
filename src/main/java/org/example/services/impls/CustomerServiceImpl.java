package org.example.services.impls;

import lombok.extern.slf4j.Slf4j;
import org.example.dtos.customers.CustomerDto;
import org.example.entities.Customer;
import org.example.entities.User;
import org.example.exceptions.NotFoundException;
import org.example.exceptions.ServiceException;
import org.example.mappers.CustomerMapper;
import org.example.repositories.CustomerRepository;
import org.example.repositories.UserRepository;
import org.example.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {
    private CustomerRepository customerRepository;

    private UserRepository userRepository;

    private CustomerMapper mapper;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, UserRepository userRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.mapper = customerMapper;
    }

    @Override
    public CustomerDto save(CustomerDto dto){
        log.debug("Request to save Customer {}", dto);
        try {
            if (userRepository.findByPhoneNumber(dto.getPhoneNumber()).isPresent()) {
                throw new IllegalArgumentException("User with such phone number: {" + dto.getPhoneNumber() + "}, already exists");
            }
            Customer saved = customerRepository.save(mapper.toEntity(dto));
            log.info("Customer saved successfully {}", saved);
            return mapper.toDto(saved);
        }
        catch (Exception e) {
            log.error("Error occurred while saving customer: {}", dto, e);
            throw new ServiceException("Could not save customer", e);
        }
    }

    @Override
    public CustomerDto findById(String id){
        log.debug("Request to find Customer by id {}", id);
        return mapper.toDto(customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Customer not found with id: " + id)));
    }

    @Override
    public CustomerDto update(CustomerDto dto){
        log.debug("Request to update Customer {}", dto);
        try {
            CustomerDto foundCustomerDto = findById(dto.getId());
            if (foundCustomerDto.getEmail().equals(dto.getEmail())) {
                Optional<User> user = userRepository.findByPhoneNumber(dto.getPhoneNumber());
                if (user.isEmpty() || user.get().getId().equals(dto.getId())) {
                    Customer updated = customerRepository.update(mapper.toEntity(dto));
                    log.info("Customer updated successfully {}", updated);
                    return mapper.toDto(updated);
                }
                throw new IllegalArgumentException("User with such phone number: {" + dto.getPhoneNumber() + "}, already exists");
            }
            throw new IllegalArgumentException("While updating customer is promised to change email");
        }
        catch (Exception e) {
            log.error("Error occurred while updating customer: {}", dto, e);
            throw new ServiceException("Could not update customer", e);
        }
    }

    @Override
    public void delete(CustomerDto dto){
        log.debug("Request to delete Customer {}", dto);
        try {
            customerRepository.delete(mapper.toEntity(dto));
            log.info("Customer deleted successfully {}", dto);

        }
        catch (Exception e) {
            log.error("Error occurred while deleting customer: {}", dto, e);
            throw new ServiceException("Could not delete customer", e);
        }
    }

    @Override
    public void deleteById(String id){
        log.debug("Request to delete Customer by id {}", id);
        try{
            delete(findById(id));
        }
        catch (Exception e) {
            log.error("Error occurred while deleting customer by id: {}", id, e);
            throw new ServiceException("Could not delete customer", e);
        }
    }

    @Override
    public Collection<CustomerDto> findAll(){
        log.debug("Request to find all Customers");
        return mapper.toDtoCollection(customerRepository.findAll());
    }
}
