package com.example.library.controller;

import com.example.library.dto.BookRequest;
import com.example.library.dto.BookResponse;
import com.example.library.model.Book;
import com.example.library.model.Genre;
import com.example.library.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private Book testBook;
    private BookRequest bookRequest;

    @BeforeEach
    void setUp() {
        testBook = new Book("Test Book", "Test Author", Genre.TECHNOLOGY, 5);
        bookRequest = new BookRequest("Test Book", "Test Author", Genre.TECHNOLOGY, 5);
    }

    @Test
    void createBook_ShouldReturnCreated() throws Exception {
        when(bookService.create(any(BookRequest.class))).thenReturn(testBook);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Book"))
                .andExpect(jsonPath("$.author").value("Test Author"));

        verify(bookService, times(1)).create(any(BookRequest.class));
    }

    @Test
    void createBook_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        BookRequest invalidRequest = new BookRequest("", "", null, 0);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(bookService, never()).create(any());
    }

    @Test
    void getAllBooks_ShouldReturnList() throws Exception {
        List<Book> books = Arrays.asList(testBook);
        when(bookService.findAll(any())).thenReturn(org.springframework.data.domain.Page.empty());

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk());

        verify(bookService, times(1)).findAll(any());
    }

    @Test
    void getBookById_ShouldReturnBook() throws Exception {
        when(bookService.findById("1")).thenReturn(testBook);

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Book"));

        verify(bookService, times(1)).findById("1");
    }

    @Test
    void updateBook_ShouldReturnUpdatedBook() throws Exception {
        when(bookService.update(eq("1"), any(BookRequest.class))).thenReturn(testBook);

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Book"));

        verify(bookService, times(1)).update(eq("1"), any(BookRequest.class));
    }

    @Test
    void deleteBook_ShouldReturnNoContent() throws Exception {
        doNothing().when(bookService).delete("1");

        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).delete("1");
    }

    @Test
    void searchByTitle_ShouldReturnMatchingBooks() throws Exception {
        List<Book> books = Arrays.asList(testBook);
        when(bookService.searchByTitle("Test")).thenReturn(books);

        mockMvc.perform(get("/api/books/search/title")
                        .param("q", "Test"))
                .andExpect(status().isOk());

        verify(bookService, times(1)).searchByTitle("Test");
    }
}
