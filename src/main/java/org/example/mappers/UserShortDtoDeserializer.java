package org.example.mappers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.dtos.customers.CustomerShortDto;
import org.example.dtos.employees.EmployeeShortDto;
import org.example.dtos.users.UserShortDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

public class UserShortDtoDeserializer extends JsonDeserializer<UserShortDto> {

    @Override
    public UserShortDto deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectNode node = p.getCodec().readTree(p);


        if (node.has("dateOfBirth")) {
            return CustomerShortDto.builder()
                    .id(node.get("id").asText())
                    .firstName(node.get("firstName").asText())
                    .lastName(node.get("lastName").asText())
                    .email(node.get("email").asText())
                    .phoneNumber(node.get("phoneNumber").asText())
                    .dateOfBirth(LocalDate.parse(node.get("dateOfBirth").asText()))
                    .build();
        } else if (node.has("salary")) {
            return EmployeeShortDto.builder()
                    .id(node.get("id").asText())
                    .firstName(node.get("firstName").asText())
                    .lastName(node.get("lastName").asText())
                    .email(node.get("email").asText())
                    .phoneNumber(node.get("phoneNumber").asText())
                    .salary(BigDecimal.valueOf(node.get("salary").asDouble()))
                    .build();
        } else {
            throw new IllegalArgumentException("Cannot determine user type");
        }
    }
}
