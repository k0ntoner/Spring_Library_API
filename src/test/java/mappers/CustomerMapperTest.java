package mappers;

import configs.TestConfig;
import org.example.dtos.customers.CustomerDto;
import org.example.dtos.customers.CustomerShortDto;
import org.example.dtos.orders.OrderDto;
import org.example.dtos.copies.BookCopyDto;
import org.example.dtos.users.UserShortDto;
import org.example.entities.BookCopy;
import org.example.entities.Customer;
import org.example.entities.Order;
import org.example.enums.Status;
import org.example.enums.SubscriptionType;
import org.example.mappers.CustomerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerMapperTest {

    private CustomerMapper customerMapper;

    @BeforeEach
    void setUp() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class);
        customerMapper = context.getBean(CustomerMapper.class);
    }

    @Test
    void toDto_shouldMapCustomerToCustomerDto() {
        // given
        Customer customer = buildCustomer();

        // when
        CustomerDto result = customerMapper.toDto(customer);

        // then
        assertNotNull(result);
        assertEquals("cust1", result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("+3801234567890", result.getPhoneNumber());
        assertEquals(LocalDate.of(1990, 1, 1), result.getDateOfBirth());
        assertNotNull(result.getOrders());
    }

    @Test
    void toEntity_shouldMapCustomerDtoToCustomer() {
        // given
        CustomerDto customerDto = buildCustomerDto();

        // when
        Customer result = customerMapper.toEntity(customerDto);

        // then
        assertNotNull(result);
        assertEquals("cust1", result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("+3801234567890", result.getPhoneNumber());
        assertEquals(LocalDate.of(1990, 1, 1), result.getDateOfBirth());
        assertNotNull(result.getOrders());
    }

    @Test
    void toDtoCollection_shouldMapListOfCustomersToListOfCustomerDtos() {
        // given
        List<Customer> customers = List.of(buildCustomer());

        // when
        List<CustomerDto> result = (List<CustomerDto>) customerMapper.toDtoCollection(customers);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("cust1", result.get(0).getId());
    }

    @Test
    void toEntityCollection_shouldMapListOfCustomerDtosToListOfCustomers() {
        // given
        List<CustomerDto> customerDtos = List.of(buildCustomerDto());

        // when
        List<Customer> result = (List<Customer>) customerMapper.toEntityCollection(customerDtos);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("cust1", result.get(0).getId());
    }

    private Customer buildCustomer() {
        return Customer.builder()
                .id("cust1")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("+3801234567890")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .orders(List.of(buildOrder()))
                .build();
    }

    private CustomerDto buildCustomerDto() {
        return CustomerDto.builder()
                .id("cust1")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("+3801234567890")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .orders(List.of(buildOrderDto()))
                .build();
    }

    private Order buildOrder() {
        return Order.builder()
                .id(1L)
                .subscriptionType(SubscriptionType.READING_ROOM)
                .orderDate(LocalDate.of(2024, 1, 1))
                .expirationDate(LocalDate.of(2024, 2, 1))
                .status(Status.BORROWED)
                .bookCopy(BookCopy.builder().id(1L).build())
                .user(null) // Для тесту мапінгу поки user не потрібен
                .build();
    }

    private OrderDto buildOrderDto() {
        return OrderDto.builder()
                .id(1L)
                .subscriptionType(SubscriptionType.READING_ROOM)
                .orderDate(LocalDate.of(2024, 1, 1))
                .expirationDate(LocalDate.of(2024, 2, 1))
                .status(Status.BORROWED)
                .bookCopyDto(BookCopyDto.builder().id(1L).build())
                .userDto(CustomerShortDto.builder()
                        .id("cust1")
                        .firstName("John")
                        .lastName("Doe")
                        .email("john.doe@example.com")
                        .phoneNumber("+3801234567890")
                        .dateOfBirth(LocalDate.of(1990, 1, 1))
                        .build())
                .build();
    }
}
