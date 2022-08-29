package com.study.spring.library.controller;

import com.study.spring.library.domain.Comment;
import com.study.spring.library.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  @PostMapping("/api/v1/books/{bookId}/comments")
  public void createComment(@PathVariable String bookId, @RequestBody Comment comment) {
    commentService.create(comment, bookId);
  }

  @DeleteMapping("/api/v1/books/{bookId}/comments/{commentId}")
  public void deleteComment(@PathVariable String bookId, @PathVariable String commentId) {
    commentService.deleteById(bookId, commentId);
  }

  @PatchMapping("/api/v1/books/{bookId}/comments/{commentId}")
  public void editComment(@PathVariable String bookId, @PathVariable String commentId, @RequestBody Comment comment) {
    commentService.update(comment, bookId);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public String handleException(Exception e) {
    return e.getMessage();
  }
}
