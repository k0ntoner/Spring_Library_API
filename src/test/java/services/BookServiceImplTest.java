package services;

import org.example.dtos.books.BookDto;
import org.example.entities.Book;
import org.example.mappers.BookCopyMapper;
import org.example.mappers.BookMapper;
import org.example.repositories.BookRepository;
import org.example.services.BookService;
import org.example.services.impls.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookServiceImplTest {

    private BookRepository mockBookRepository;
    private BookService bookService;
    private BookMapper mockBookMapper;

    @BeforeEach
    public void setUp() throws SQLException {
        mockBookRepository = Mockito.mock(BookRepository.class);
        mockBookMapper = Mockito.mock(BookMapper.class);
        bookService = new BookServiceImpl(mockBookRepository, mockBookMapper);
        when(mockBookRepository.isTitleExists(any())).thenReturn(false);
        when(mockBookRepository.isCopiesOfBookBorrowed(any())).thenReturn(false);
        when(mockBookRepository.findById(any())).thenReturn(Optional.of(buildBook()));
        when(mockBookMapper.toDto(any())).thenReturn(buildBookDto());
        when(mockBookMapper.toEntity(any())).thenReturn(buildBook());
    }
    private BookDto buildBookDto() {
        return BookDto.builder()
                .id(1L)
                .title("testBook")
                .author("testAuthor")
                .description("testDescription")
                .build();
    }

    private Book buildBook(){

        return Book.builder()
                .id(1L)
                .title("testBook")
                .author("testAuthor")
                .description("testDescription")
                .build();
    }

    @Test
    @DisplayName("Should return saved book")
    public void save_ShouldReturnSavedBook() throws SQLException {
        //Given
        when(mockBookRepository.save(any())).thenReturn(buildBook());

        //When
        bookService.save(buildBookDto());

        //Then
        verify(mockBookRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Should return updated book")
    public void update_ShouldReturnUpdatedBook() throws SQLException {
        //Given
        when(mockBookRepository.update(any())).thenReturn(buildBook());

        //When
        bookService.update(buildBookDto());

        //Then
        verify(mockBookRepository, times(1)).update(any());
    }

    @Test
    @DisplayName("Should return book by id")
    public void findById_ShouldReturnBookById() throws SQLException {
        //Given

        when(mockBookRepository.findById(any())).thenReturn(Optional.of(buildBook()));
        //When
        bookService.findById(buildBookDto().getId());

        //Then
        verify(mockBookRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("Should delete book")
    public void delete_ShouldDeleteBook() throws SQLException {
        //Given

        //When
        bookService.delete(buildBookDto());

        //Then
        verify(mockBookRepository, times(1)).delete(any());
    }

    @Test
    @DisplayName("Should return all books")
    public void findAll_ShouldReturnAllBooks() throws SQLException {
        //Given
        when(mockBookRepository.findAll()).thenReturn(new ArrayList<>());

        //When
        bookService.findAll();

        //Then
        verify(mockBookRepository, times(1)).findAll();
    }
}
