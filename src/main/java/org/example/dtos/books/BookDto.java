package org.example.dtos.books;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {
    private Long id;

    @NotNull
    @Size(min = 1, max = 30)
    private String title;

    @Size(max = 255)
    private String description;

    @NotNull
    @Size(max = 30)
    private String author;
}
