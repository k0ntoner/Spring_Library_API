package org.example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.Status;

import java.util.List;

@Entity
@Table(name="book_copies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookCopy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="book_id", nullable = false)
    private Book book;

    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable=false)
    private Status status;

    @OneToMany(mappedBy = "bookCopy", cascade = CascadeType.REMOVE)
    private List<Order> orders;
}
