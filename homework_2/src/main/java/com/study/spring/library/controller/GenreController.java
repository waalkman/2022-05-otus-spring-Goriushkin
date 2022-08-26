package com.study.spring.library.controller;

import com.study.spring.library.domain.Genre;
import com.study.spring.library.service.GenreService;
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
public class GenreController {

  private final GenreService genreService;

  @GetMapping("/api/v1/genres")
  public Collection<Genre> listGenres() {
    return genreService.getAll();
  }

  @PostMapping("/api/v1/genres")
  public Genre createGenre(@Valid @RequestBody Genre genre) {
    return genreService.create(genre);
  }

  @PatchMapping("/api/v1/genres")
  public void updateGenre(@Valid @RequestBody Genre genre) {
    genreService.update(genre);
  }

  @DeleteMapping("/api/v1/genres/{id}")
  public void deleteGenre(@PathVariable("id") String genreId) {
    genreService.deleteById(genreId);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public String handleException(Exception e) {
    return e.getMessage();
  }
}
