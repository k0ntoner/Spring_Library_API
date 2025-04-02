package org.example.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.Status;
import org.example.enums.SubscriptionType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable=false )
    private User user;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "copy_id", nullable=false)
    private BookCopy bookCopy;

    @Enumerated(EnumType.STRING)
    @Column(name= "subscription_type", nullable=false)
    private SubscriptionType subscriptionType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name="order_date", nullable=false)
    private LocalDate orderDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name="expiration_date", nullable=false)
    private LocalDate expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(name= "status", nullable=false)
    private Status status;
}
