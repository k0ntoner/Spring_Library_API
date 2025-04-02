package org.example.services.impls;

import lombok.extern.slf4j.Slf4j;
import org.example.dtos.customers.CustomerRegistrationDto;
import org.example.dtos.employees.EmployeeDto;
import org.example.dtos.employees.EmployeeRegistrationDto;
import org.example.dtos.users.LoginUserDto;
import org.example.dtos.users.UserDto;
import org.example.dtos.users.UserRegistrationDto;
import org.example.enums.Role;
import org.example.exceptions.ServiceException;
import org.example.mappers.CustomerMapper;
import org.example.mappers.EmployeeMapper;
import org.example.mappers.UserMapper;
import org.example.services.AuthService;
import org.example.services.CustomerService;
import org.example.services.EmployeeService;
import org.example.services.UserService;
import org.example.utils.Auth0Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:application.properties")
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserService userService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Value("${auth0.domain}")
    private String domain;

    @Value("${auth0.audience}")
    private String audience;

    @Value("${auth0.connection}")
    private String connection;

    @Value("${auth0.client_id_reg}")
    private String clientIdReg;

    @Value("${auth0.client_secret_reg}")
    private String clientSecretReg;

    @Value("${auth0.client_id_log}")
    private String clientIdLog;

    @Value("${auth0.client_secret_log}")
    private String clientSecretLog;

    @Override
    public String login(LoginUserDto loginUserDto) {
        log.debug("Request to login using auth0");
        try {
            return Auth0Util.login(loginUserDto.getEmail(), loginUserDto.getPassword(), domain, clientIdLog, clientSecretLog, audience);
        } catch (Exception e) {
            log.error("Error while logging using Auth0", e);
            throw new ServiceException("Could not login using Auth0", e);
        }
    }

    @Override
    public UserDto register(UserRegistrationDto userRegistrationDto) {
        log.info("Request to register using auth0");
        try {
            if (userService.isPhoneNumberExists(userRegistrationDto.getPhoneNumber())) {
                throw new IllegalStateException("Phone number already exists");
            }

            if (userRegistrationDto instanceof EmployeeRegistrationDto employeeRegistrationDto) {
                String auth0UserId = Auth0Util.register(employeeRegistrationDto.getEmail(), employeeRegistrationDto.getPassword(), Role.ROLE_EMPLOYEE, domain, clientIdReg, clientSecretReg, connection, audience);
                userRegistrationDto.setId(auth0UserId);
                return employeeService.save(employeeMapper.toDto(employeeRegistrationDto));
            } else if (userRegistrationDto instanceof CustomerRegistrationDto customerRegistrationDto) {
                String auth0UserId = Auth0Util.register(customerRegistrationDto.getEmail(), customerRegistrationDto.getPassword(), Role.ROLE_CUSTOMER, domain, clientIdReg, clientSecretReg, connection, audience);
                userRegistrationDto.setId(auth0UserId);
                return customerService.save(customerMapper.toDto(customerRegistrationDto));
            }
            throw new IllegalStateException("Invalid type of registration user");

        } catch (Exception e) {
            log.error("Error while registration using Auth0", e);
            throw new ServiceException("Could not register using Auth0", e);
        }
    }

    @Override
    public void logout() {

    }
}
