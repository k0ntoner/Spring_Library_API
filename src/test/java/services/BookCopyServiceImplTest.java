package services;

import org.example.dtos.books.BookDto;
import org.example.dtos.copies.BookCopyDto;
import org.example.entities.Book;
import org.example.entities.BookCopy;
import org.example.enums.Status;
import org.example.mappers.BookCopyMapper;
import org.example.repositories.BookCopyRepository;
import org.example.services.BookCopyService;
import org.example.services.impls.BookCopyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class BookCopyServiceImplTest {
    private BookCopyRepository mockBookCopyRepository;
    private BookCopyService bookCopyService;
    private BookCopyMapper mockBookCopyMapper;

    @BeforeEach
    public void setUp() throws SQLException {
        mockBookCopyRepository = Mockito.mock(BookCopyRepository.class);
        mockBookCopyMapper = Mockito.mock(BookCopyMapper.class);

        bookCopyService = new BookCopyServiceImpl(mockBookCopyRepository,mockBookCopyMapper);
        when(mockBookCopyRepository.isBookCopyAvailable(any())).thenReturn(true);
        when(mockBookCopyRepository.findById(any())).thenReturn(Optional.of(buildBookCopy()));
        when(mockBookCopyMapper.toDto(any())).thenReturn(buildBookCopyDto());
        when(mockBookCopyMapper.toEntity(any())).thenReturn(buildBookCopy());
    }

    private Book buildBook(){

        return Book.builder()
                .id(1L)
                .title("testBook")
                .author("testAuthor")
                .description("testDescription")
                .build();
    }

    private BookDto buildBookDto(){
        return BookDto.builder()
                .id(1L)
                .title("testBook")
                .author("testAuthor")
                .description("testDescription")
                .build();
    }

    private BookCopyDto buildBookCopyDto(){
        return BookCopyDto.builder()
                .id(1L)
                .status(Status.AVAILABLE)
                .bookDto(buildBookDto())
                .build();
    }
    private BookCopy buildBookCopy(){

        return BookCopy.builder()
                .id(1L)
                .status(Status.AVAILABLE)
                .book(buildBook())
                .build();
    }


    @Test
    @DisplayName("Should return saved copy of book")
    public void save_ShouldReturnSavedBookCopy() throws SQLException {
        //Given
        when(mockBookCopyRepository.save(any())).thenReturn(buildBookCopy());

        //When
        bookCopyService.save(buildBookCopyDto());

        //Then
        verify(mockBookCopyRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Should return updated copy of book")
    public void update_ShouldReturnUpdatedBookCopy() throws SQLException {
        //Given
        when(mockBookCopyRepository.update(any())).thenReturn(buildBookCopy());

        //When
        bookCopyService.update(buildBookCopyDto());

        //Then
        verify(mockBookCopyRepository, times(1)).update(any());
    }

    @Test
    @DisplayName("Should return copy of book by id")
    public void findById_ShouldReturnBookCopyById() throws SQLException {
        //Given
        when(mockBookCopyRepository.findById(any())).thenReturn(Optional.of(buildBookCopy()));
        //When
        bookCopyService.findById(buildBookCopyDto().getId());

        //Then
        verify(mockBookCopyRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("Should delete copy of book")
    public void delete_ShouldDeleteBookCopy() throws SQLException {
        //Given

        //When
        bookCopyService.delete(buildBookCopyDto());

        //Then
        verify(mockBookCopyRepository, times(1)).delete(any());
    }

    @Test
    @DisplayName("Should return all copies of book")
    public void findAll_ShouldReturnAllBookCopies() throws SQLException {
        //Given
        when(mockBookCopyRepository.findAll()).thenReturn(new ArrayList<>());

        //When
        bookCopyService.findAll();

        //Then
        verify(mockBookCopyRepository, times(1)).findAll();
    }
}
