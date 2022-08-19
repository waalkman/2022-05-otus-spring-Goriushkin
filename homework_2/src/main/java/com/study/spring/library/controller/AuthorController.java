package com.study.spring.library.controller;

import com.study.spring.library.domain.Author;
import com.study.spring.library.service.AuthorService;
import java.util.Collection;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthorController {

  private final AuthorService authorService;

  @GetMapping("/authors")
  public String listAuthors(Model model) {
    Collection<Author> authors = authorService.getAll();
    model.addAttribute("authors", authors);
    return "authors";
  }

  @PostMapping("/authors")
  public String createAuthor(@Valid @ModelAttribute Author author) {
    authorService.create(author);
    return "redirect:/authors";
  }

  @GetMapping("/authors/edit/{authorId}")
  public String editAuthor(@PathVariable String authorId, Model model) {
    Author author = authorService.findById(authorId);
    model.addAttribute("author", author);
    return "author_edit";
  }

  @PostMapping("/authors/edit")
  public String updateAuthor(@Valid @ModelAttribute Author author) {
    authorService.update(author);
    return "redirect:/authors";
  }

  @PostMapping("/authors/delete")
  public String deleteAuthor(@RequestParam("authorId") String authorId) {
    authorService.deleteById(authorId);
    return "redirect:/authors";
  }

  @ExceptionHandler(Exception.class)
  public String handleException(Model model, Exception e) {
    model.addAttribute("message", e.getMessage());
    return "error";
  }
}
