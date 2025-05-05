package org.example.dtos.employees;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.dtos.users.UserShortDto;
import org.example.entities.Employee;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class EmployeeShortDto extends UserShortDto {
    @NotBlank
    @Digits(integer = 10, fraction = 2, message = "Salary must have at most 10 digits in the integer part and 2 decimal places")
    private BigDecimal salary;
}
