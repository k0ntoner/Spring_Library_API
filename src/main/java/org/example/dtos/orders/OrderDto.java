package org.example.dtos.orders;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dtos.copies.BookCopyDto;
import org.example.dtos.users.UserShortDto;
import org.example.entities.BookCopy;
import org.example.entities.User;
import org.example.enums.Status;
import org.example.enums.SubscriptionType;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;

    @NotNull(message = "User cannot be blank")
    private UserShortDto userDto;

    @NotNull(message = "Copy of book cannot be blank")
    private BookCopyDto bookCopyDto;

    @NotNull(message = "Subscription type cannot be blank")
    private SubscriptionType subscriptionType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "Order date type cannot be blank")
    private LocalDate orderDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "Expiration date type cannot be blank")
    private LocalDate expirationDate;

    @NotNull(message = "Status type cannot be blank")
    private Status status;

}
