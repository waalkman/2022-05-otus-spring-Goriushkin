package com.study.spring.library.controller;

import com.study.spring.library.domain.Comment;
import com.study.spring.library.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  @PostMapping("/books/{bookId}/comments")
  public String createComment(@PathVariable String bookId, @ModelAttribute Comment comment) {
    commentService.create(comment, bookId);
    return "redirect:/books/" + bookId;
  }

  @PostMapping("/books/{bookId}/comments/{commentId}/delete")
  public String deleteComment(@PathVariable String bookId, @PathVariable String commentId) {
    commentService.deleteById(bookId, commentId);
    return "redirect:/books/" + bookId;
  }

  @GetMapping("/books/{bookId}/comments/{commentId}/edit")
  public String editCommentPage(@PathVariable String bookId, @PathVariable String commentId, Model model) {
    Comment comment = commentService.findById(bookId, commentId);
    model.addAttribute("comment", comment);
    model.addAttribute("bookId", bookId);
    return "comment_edit";
  }

  @PostMapping("/books/{bookId}/comments/{commentId}/edit")
  public String editComment(@PathVariable String bookId, @PathVariable String commentId, @ModelAttribute Comment comment) {
    commentService.update(comment, bookId);
    return "redirect:/books/" + bookId;
  }

  @ExceptionHandler(Exception.class)
  public String handleException(Model model, Exception e) {
    model.addAttribute("message", e.getMessage());
    return "error";
  }
}
