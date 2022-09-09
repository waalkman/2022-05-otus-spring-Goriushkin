package com.study.spring.library.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.study.spring.library.domain.Genre;
import com.study.spring.library.service.GenreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = GenreController.class, properties = {"mongock.enabled=false"})
class GenreControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private GenreService genreService;

  @WithMockUser
  @Test
  void listGenres_success() throws Exception {
    mockMvc.perform(get("/genres"))
           .andExpect(status().isOk());
  }

  @WithAnonymousUser
  @Test
  void listGenres_unauthorized() throws Exception {
    mockMvc.perform(get("/genres"))
           .andExpect(status().isUnauthorized());
  }

  @WithMockUser
  @Test
  void createGenre_success() throws Exception {
    Genre dummyGenre = Genre.builder()
                            .id("@!3")
                            .name("name")
                            .build();

    mockMvc.perform(post("/genres").with(csrf()).flashAttr("genre", dummyGenre))
           .andExpect(status().isFound());
  }

  @WithAnonymousUser
  @Test
  void createGenre_unauthorized() throws Exception {
    mockMvc.perform(post("/genres").with(csrf()))
           .andExpect(status().isUnauthorized());
  }

  @WithMockUser
  @Test
  void editGenre_success() throws Exception {
    when(genreService.findById(any())).thenReturn(
        Genre.builder()
             .id("123")
             .name("name")
             .build());

    mockMvc.perform(get("/genres/edit/123"))
           .andExpect(status().isOk());
  }

  @WithAnonymousUser
  @Test
  void editGenre_unauthorized() throws Exception {
    mockMvc.perform(get("/genres/edit/123"))
           .andExpect(status().isUnauthorized());
  }

  @WithMockUser
  @Test
  void updateGenre_success() throws Exception {
    Genre dummyGenre = Genre.builder()
                            .id("123")
                            .name("name")
                            .build();

    mockMvc.perform(post("/genres/edit").with(csrf()).flashAttr("genre", dummyGenre))
           .andExpect(status().isFound());
  }

  @WithAnonymousUser
  @Test
  void updateGenre_unauthorized() throws Exception {
    mockMvc.perform(post("/genres/edit").with(csrf()))
           .andExpect(status().isUnauthorized());
  }

  @WithMockUser
  @Test
  void deleteGenre_success() throws Exception {
    mockMvc.perform(post("/genres/delete").with(csrf()).param("genreId", "123124"))
           .andExpect(status().isFound());
  }

  @WithAnonymousUser
  @Test
  void deleteGenre_unauthorized() throws Exception {
    mockMvc.perform(post("/genres/delete").with(csrf()))
           .andExpect(status().isUnauthorized());
  }
}