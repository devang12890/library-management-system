package com.example.library.service;

import com.example.library.dto.BookRequest;
import com.example.library.exception.BookNotFoundException;
import com.example.library.model.Book;
import com.example.library.model.Genre;
import com.example.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book testBook;
    private BookRequest bookRequest;

    @BeforeEach
    void setUp() {
        testBook = new Book("Test Book", "Test Author", Genre.TECHNOLOGY, 5);
        bookRequest = new BookRequest("Test Book", "Test Author", Genre.TECHNOLOGY, 5);
    }

    @Test
    void create_ShouldSaveAndReturnBook() {
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        Book result = bookService.create(bookRequest);

        assertNotNull(result);
        assertEquals("Test Book", result.getTitle());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void findById_WhenBookExists_ShouldReturnBook() {
        when(bookRepository.findById("1")).thenReturn(Optional.of(testBook));

        Book result = bookService.findById("1");

        assertNotNull(result);
        assertEquals("Test Book", result.getTitle());
        verify(bookRepository, times(1)).findById("1");
    }

    @Test
    void findById_WhenBookNotFound_ShouldThrowException() {
        when(bookRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.findById("1"));
        verify(bookRepository, times(1)).findById("1");
    }

    @Test
    void update_ShouldUpdateAndReturnBook() {
        when(bookRepository.findById("1")).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        Book result = bookService.update("1", bookRequest);

        assertNotNull(result);
        verify(bookRepository, times(1)).findById("1");
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void delete_WhenBookExists_ShouldDeleteBook() {
        when(bookRepository.existsById("1")).thenReturn(true);
        doNothing().when(bookRepository).deleteById("1");

        bookService.delete("1");

        verify(bookRepository, times(1)).existsById("1");
        verify(bookRepository, times(1)).deleteById("1");
    }

    @Test
    void delete_WhenBookNotFound_ShouldThrowException() {
        when(bookRepository.existsById("1")).thenReturn(false);

        assertThrows(BookNotFoundException.class, () -> bookService.delete("1"));
        verify(bookRepository, times(1)).existsById("1");
        verify(bookRepository, never()).deleteById(any());
    }

    @Test
    void searchByTitle_ShouldReturnMatchingBooks() {
        List<Book> books = Arrays.asList(testBook);
        when(bookRepository.findByTitleContainingIgnoreCase("Test")).thenReturn(books);

        List<Book> result = bookService.searchByTitle("Test");

        assertEquals(1, result.size());
        verify(bookRepository, times(1)).findByTitleContainingIgnoreCase("Test");
    }

    @Test
    void findAll_WithPagination_ShouldReturnPage() {
        Page<Book> page = new PageImpl<>(Arrays.asList(testBook));
        when(bookRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<Book> result = bookService.findAll(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(bookRepository, times(1)).findAll(any(PageRequest.class));
    }
}
