package org.example.dtos.copies;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dtos.books.BookDto;
import org.example.enums.Status;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookCopyDto {

    private Long id;

    @NotBlank(message = "Book cannot be blank") 
    private BookDto bookDto;

    @NotBlank(message = "Status cannot be blank")
    private Status status;
}
