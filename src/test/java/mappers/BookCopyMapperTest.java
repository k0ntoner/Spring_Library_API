package mappers;

import configs.TestConfig;
import org.example.configs.WebConfig;
import org.example.dtos.copies.BookCopyDto;
import org.example.dtos.books.BookDto;
import org.example.entities.Book;
import org.example.entities.BookCopy;
import org.example.enums.Status;
import org.example.mappers.BookCopyMapper;
import org.example.mappers.BookCopyMapperImpl;
import org.example.mappers.BookMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookCopyMapperTest {
    private BookCopyMapper bookCopyMapper;


    @BeforeEach
    void setUp() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class);
        bookCopyMapper = context.getBean(BookCopyMapper.class);
    }

    @Test
    void toDto_shouldMapBookCopyToBookCopyDto() {
        // given
        Book book = Book.builder()
                .id(1L)
                .title("Sample Title")
                .description("Sample Description")
                .author("Sample Author")
                .build();

        BookCopy bookCopy = BookCopy.builder()
                .id(100L)
                .book(book)
                .status(Status.AVAILABLE)
                .build();

        // when
        BookCopyDto result = bookCopyMapper.toDto(bookCopy);

        // then
        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals(Status.AVAILABLE, result.getStatus());

        assertNotNull(result.getBookDto());
        assertEquals(1L, result.getBookDto().getId());
        assertEquals("Sample Title", result.getBookDto().getTitle());
        assertEquals("Sample Description", result.getBookDto().getDescription());
        assertEquals("Sample Author", result.getBookDto().getAuthor());
    }

    @Test
    void toEntity_shouldMapBookCopyDtoToBookCopy() {
        // given
        BookDto bookDto = BookDto.builder()
                .id(1L)
                .title("Sample Title")
                .description("Sample Description")
                .author("Sample Author")
                .build();

        BookCopyDto bookCopyDto = BookCopyDto.builder()
                .id(100L)
                .bookDto(bookDto)
                .status(Status.AVAILABLE)
                .build();

        // when
        BookCopy result = bookCopyMapper.toEntity(bookCopyDto);

        // then
        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals(Status.AVAILABLE, result.getStatus());

        assertNotNull(result.getBook());
        assertEquals(1L, result.getBook().getId());
        assertEquals("Sample Title", result.getBook().getTitle());
        assertEquals("Sample Description", result.getBook().getDescription());
        assertEquals("Sample Author", result.getBook().getAuthor());
    }

    @Test
    void toDtoCollection_shouldMapListOfBookCopiesToListOfBookCopyDtos() {
        // given
        BookCopy bookCopy = BookCopy.builder()
                .id(100L)
                .book(Book.builder()
                        .id(1L)
                        .title("Title")
                        .description("Description")
                        .author("Author")
                        .build())
                .status(Status.AVAILABLE)
                .build();

        List<BookCopy> bookCopies = List.of(bookCopy);

        // when
        List<BookCopyDto> result = (List<BookCopyDto>) bookCopyMapper.toDtoCollection(bookCopies);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100L, result.get(0).getId());
        assertNotNull(result.get(0).getBookDto());
    }

    @Test
    void toEntityCollection_shouldMapListOfBookCopyDtosToListOfBookCopies() {
        // given
        BookCopyDto bookCopyDto = BookCopyDto.builder()
                .id(100L)
                .bookDto(BookDto.builder()
                        .id(1L)
                        .title("Title")
                        .description("Description")
                        .author("Author")
                        .build())
                .status(Status.AVAILABLE)
                .build();

        List<BookCopyDto> bookCopyDtos = List.of(bookCopyDto);

        // when
        List<BookCopy> result = (List<BookCopy>) bookCopyMapper.toEntityCollection(bookCopyDtos);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100L, result.get(0).getId());
        assertNotNull(result.get(0).getBook());
    }
}
