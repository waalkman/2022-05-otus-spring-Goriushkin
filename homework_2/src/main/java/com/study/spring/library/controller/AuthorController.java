package com.study.spring.library.controller;

import com.study.spring.library.domain.Author;
import com.study.spring.library.service.AuthorService;
import java.util.Collection;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthorController {

  private final AuthorService authorService;

  @GetMapping("/api/v1/authors")
  public Collection<Author> listAuthors() {
    return authorService.getAll();
  }

  @PostMapping("/api/v1/authors")
  public Author createAuthor(@Valid @RequestBody Author author) {
    return authorService.create(author);
  }

  @PatchMapping("/api/v1/authors")
  public void updateAuthor(@Valid @RequestBody Author author) {
    authorService.update(author);
  }

  @DeleteMapping("/api/v1/authors/{id}")
  public void deleteAuthor(@PathVariable("id") String authorId) {
    authorService.deleteById(authorId);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public String handleException(Exception e) {
    return e.getMessage();
  }
}
