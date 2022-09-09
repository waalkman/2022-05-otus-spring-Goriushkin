package com.study.spring.library.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.study.spring.library.domain.Comment;
import com.study.spring.library.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = CommentController.class, properties = {"mongock.enabled=false"})
class CommentControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private CommentService commentService;

  @WithMockUser
  @Test
  void createComment_success() throws Exception {
    mockMvc.perform(post("/books/123/comments").with(csrf()))
           .andExpect(status().isFound());
  }

  @WithAnonymousUser
  @Test
  void createComment_unauthorized() throws Exception {
    mockMvc.perform(post("/books/123/comments").with(csrf()))
           .andExpect(status().isUnauthorized());
  }

  @WithMockUser
  @Test
  void deleteComment_success() throws Exception {
    mockMvc.perform(post("/books/123/comments/543412/delete").with(csrf()))
           .andExpect(status().isFound());
  }

  @WithAnonymousUser
  @Test
  void deleteComment_unauthorized() throws Exception {
    mockMvc.perform(post("/books/213/comments/4312/delete").with(csrf()))
           .andExpect(status().isUnauthorized());
  }

  @WithMockUser
  @Test
  void editCommentPage_success() throws Exception {
    when(commentService.findById(any(), any())).thenReturn(createDummyComment());
    mockMvc.perform(get("/books/3123/comments/123123/edit"))
           .andExpect(status().isOk());
  }

  @WithAnonymousUser
  @Test
  void editCommentPage_unauthorized() throws Exception {
    mockMvc.perform(get("/books/3123/comments/123123/edit"))
           .andExpect(status().isUnauthorized());
  }

  @WithMockUser
  @Test
  void editComment_success() throws Exception {
    mockMvc.perform(post("/books/23123/comments/875/edit").with(csrf()))
           .andExpect(status().isFound());
  }

  @WithAnonymousUser
  @Test
  void editComment_unauthorized() throws Exception {
    mockMvc.perform(post("/books/23123/comments/875/edit").with(csrf()))
           .andExpect(status().isUnauthorized());
  }

  private Comment createDummyComment() {
    return Comment.builder()
                  .id("123123")
                  .userName("u name")
                  .text("comment text")
                  .build();
  }
}