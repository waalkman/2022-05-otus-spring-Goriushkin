package com.study.spring.library.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.study.spring.library.domain.Author;
import com.study.spring.library.domain.Book;
import com.study.spring.library.domain.Genre;
import com.study.spring.library.service.AuthorService;
import com.study.spring.library.service.BookService;
import com.study.spring.library.service.GenreService;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = BookController.class, properties = {"mongock.enabled=false"})
class BookControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private AuthorService authorService;
  @MockBean
  private GenreService genreService;
  @MockBean
  private BookService bookService;

  @WithMockUser
  @Test
  void listBooks_success() throws Exception {
    mockMvc.perform(get("/books"))
           .andExpect(status().isOk());
  }

  @WithAnonymousUser
  @Test
  void listBooks_unauthorized() throws Exception {
    mockMvc.perform(get("/books"))
           .andExpect(status().isUnauthorized());
  }

  @WithMockUser
  @Test
  void showBook_success() throws Exception {
    when(bookService.findById(any())).thenReturn(createDummyBook());
    mockMvc.perform(get("/books/123"))
        .andExpect(status().isOk());
  }

  @WithAnonymousUser
  @Test
  void showBook_unauthorized() throws Exception {
    mockMvc.perform(get("/books/123"))
           .andExpect(status().isUnauthorized());
  }

  @WithMockUser
  @Test
  void deleteBook_success() throws Exception {
    mockMvc.perform(post("/books/delete").with(csrf()).param("bookId", "123"))
           .andExpect(status().isFound());
  }

  @WithAnonymousUser
  @Test
  void deleteBook_unauthorized() throws Exception {
    mockMvc.perform(post("/books/delete").with(csrf()).param("bookId", "123"))
           .andExpect(status().isUnauthorized());
  }

  @WithMockUser
  @Test
  void editBookPage_success() throws Exception {
    when(authorService.getAll()).thenReturn(Collections.singletonList(createDummyAuthor()));
    when(genreService.getAll()).thenReturn(Collections.singletonList(createDummyGenre()));
    when(bookService.findById(any())).thenReturn(createDummyBook());

    mockMvc.perform(get("/books/edit/756"))
           .andExpect(status().isOk());
  }

  @WithAnonymousUser
  @Test
  void editBookPage_unauthorized() throws Exception {
    mockMvc.perform(get("/books/edit/756"))
           .andExpect(status().isUnauthorized());
  }

  @WithMockUser
  @Test
  void updateBook_success() throws Exception {
    mockMvc.perform(post("/books/edit").with(csrf()))
           .andExpect(status().isFound());
  }

  @WithAnonymousUser
  @Test
  void updateBook_unauthorized() throws Exception {
    mockMvc.perform(post("/books/edit").with(csrf()))
           .andExpect(status().isUnauthorized());
  }

  @WithMockUser
  @Test
  void createBookPage_success() throws Exception {
    mockMvc.perform(post("/books").with(csrf()))
           .andExpect(status().isFound());
  }

  @WithAnonymousUser
  @Test
  void createBookPage_unauthorized() throws Exception {
    mockMvc.perform(post("/books").with(csrf()))
           .andExpect(status().isUnauthorized());
  }

  private Author createDummyAuthor() {
    return Author.builder().id("123").name("a name").build();
  }

  private Genre createDummyGenre() {
    return Genre.builder().id("3212").name("g name").build();
  }

  private Book createDummyBook() {
    Author author = createDummyAuthor();
    Genre genre = createDummyGenre();
    return Book.builder()
               .id("r231")
               .title("b title")
               .description("b descr")
               .author(author)
               .genre(genre)
               .build();
  }
}