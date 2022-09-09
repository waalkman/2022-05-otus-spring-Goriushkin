package com.study.spring.library.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.study.spring.library.domain.Author;
import com.study.spring.library.service.AuthorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = AuthorController.class, properties = {"mongock.enabled=false"})
class AuthorControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private AuthorService authorService;

  @WithMockUser
  @Test
  void listAuthorsAuthenticated_success() throws Exception {
    mockMvc.perform(get("/authors"))
           .andExpect(status().isOk());
  }

  @WithAnonymousUser
  @Test
  void listAuthorsAuthenticated_notAuthorized() throws Exception {
    mockMvc.perform(get("/authors"))
           .andExpect(status().isUnauthorized());
  }

  @WithMockUser
  @Test
  void createAuthorAuthenticated_success() throws Exception {
    Author dummyAuthor = Author.builder()
                               .id("123")
                               .name("name")
                               .build();

    mockMvc.perform(post("/authors").with(csrf()).flashAttr("author", dummyAuthor))
           .andExpect(status().isFound());
  }

  @WithAnonymousUser
  @Test
  void createAuthorAuthenticated_notAuthorized() throws Exception {
    mockMvc.perform(post("/authors").with(csrf()))
           .andExpect(status().isUnauthorized());
  }

  @WithMockUser
  @Test
  void editAuthorAuthenticated_success() throws Exception {
    Author dummyAuthor = Author.builder()
                               .id("123")
                               .name("name")
                               .build();

    when(authorService.findById(any())).thenReturn(dummyAuthor);
    mockMvc.perform(get("/authors/edit/123"))
           .andExpect(status().isOk());
  }

  @WithAnonymousUser
  @Test
  void editAuthorAuthenticated_notAuthorized() throws Exception {
    mockMvc.perform(get("/authors/edit/123"))
           .andExpect(status().isUnauthorized());
  }

  @WithMockUser
  @Test
  void updateAuthorAuthenticated_success() throws Exception {
    Author dummyAuthor = Author.builder()
                               .id("123")
                               .name("name")
                               .build();

    mockMvc.perform(post("/authors/edit").with(csrf()).flashAttr("author", dummyAuthor))
           .andExpect(status().isFound());
  }

  @WithAnonymousUser
  @Test
  void updateAuthorAuthenticated_notAuthorized() throws Exception {
    mockMvc.perform(post("/authors/edit").with(csrf()))
           .andExpect(status().isUnauthorized());
  }

  @WithMockUser
  @Test
  void deleteAuthorAuthenticated_success() throws Exception {
    mockMvc.perform(post("/authors/delete").with(csrf()).param("authorId", "123"))
           .andExpect(status().isFound());
  }

  @WithAnonymousUser
  @Test
  void deleteAuthorAuthenticated_notAuthorized() throws Exception {
    mockMvc.perform(post("/authors/delete").with(csrf()))
           .andExpect(status().isUnauthorized());
  }

}