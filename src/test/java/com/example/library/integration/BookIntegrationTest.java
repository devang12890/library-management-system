package com.example.library.integration;

import com.example.library.dto.BookRequest;
import com.example.library.model.Book;
import com.example.library.model.Genre;
import com.example.library.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class BookIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
    }

    @Test
    void createAndRetrieveBook_ShouldWork() throws Exception {
        BookRequest bookRequest = new BookRequest("Integration Test Book", "Test Author", Genre.TECHNOLOGY, 3);

        // Create book
        String response = mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Integration Test Book"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Verify book was saved
        assertEquals(1, bookRepository.count());
    }

    @Test
    void createBook_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        BookRequest invalidRequest = new BookRequest("", "", null, -1);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBook_ShouldWork() throws Exception {
        // Create a book first
        Book book = new Book("Original Title", "Original Author", Genre.FICTION, 5);
        book = bookRepository.save(book);
        String bookId = book.getId();

        // Update the book
        BookRequest updateRequest = new BookRequest("Updated Title", "Updated Author", Genre.TECHNOLOGY, 10);

        mockMvc.perform(put("/api/books/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.author").value("Updated Author"));
    }

    @Test
    void deleteBook_ShouldWork() throws Exception {
        // Create a book first
        Book book = new Book("To Delete", "Author", Genre.FICTION, 1);
        book = bookRepository.save(book);
        String bookId = book.getId();

        // Delete the book
        mockMvc.perform(delete("/api/books/" + bookId))
                .andExpect(status().isNoContent());

        // Verify book was deleted
        assertEquals(0, bookRepository.count());
    }
}
