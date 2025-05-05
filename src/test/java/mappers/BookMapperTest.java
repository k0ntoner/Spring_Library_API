package mappers;

import configs.TestConfig;
import org.example.dtos.books.BookDto;
import org.example.entities.Book;
import org.example.mappers.BookMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookMapperTest {

    private BookMapper bookMapper;

    @BeforeEach
    void setUp() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class);
        bookMapper = context.getBean(BookMapper.class);
    }

    @Test
    void toDto_shouldMapBookToBookDto() {
        // given
        Book book = Book.builder()
                .id(1L)
                .title("Sample Title")
                .description("Sample Description")
                .author("Sample Author")
                .build();

        // when
        BookDto result = bookMapper.toDto(book);

        // then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Sample Title", result.getTitle());
        assertEquals("Sample Description", result.getDescription());
        assertEquals("Sample Author", result.getAuthor());
    }

    @Test
    void toEntity_shouldMapBookDtoToBook() {
        // given
        BookDto bookDto = BookDto.builder()
                .id(1L)
                .title("Sample Title")
                .description("Sample Description")
                .author("Sample Author")
                .build();

        // when
        Book result = bookMapper.toEntity(bookDto);

        // then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Sample Title", result.getTitle());
        assertEquals("Sample Description", result.getDescription());
        assertEquals("Sample Author", result.getAuthor());
    }

    @Test
    void toDtoCollection_shouldMapListOfBooksToListOfBookDtos() {
        // given
        Book book = Book.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .author("Author")
                .build();

        List<Book> books = List.of(book);

        // when
        List<BookDto> result = (List<BookDto>) bookMapper.toDtoCollection(books);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Title", result.get(0).getTitle());
    }

    @Test
    void toEntityCollection_shouldMapListOfBookDtosToListOfBooks() {
        // given
        BookDto bookDto = BookDto.builder()
                .id(1L)
                .title("Title")
                .description("Description")
                .author("Author")
                .build();

        List<BookDto> bookDtos = List.of(bookDto);

        // when
        List<Book> result = (List<Book>) bookMapper.toEntityCollection(bookDtos);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Title", result.get(0).getTitle());
    }
}
