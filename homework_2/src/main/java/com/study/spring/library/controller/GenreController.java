package com.study.spring.library.controller;

import com.study.spring.library.domain.Genre;
import com.study.spring.library.service.GenreService;
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
public class GenreController {

  private final GenreService genreService;

  @GetMapping("/genres")
  public String listGenres(Model model) {
    Collection<Genre> genres = genreService.getAll();
    model.addAttribute("genres", genres);
    return "genres";
  }

  @PostMapping("/genres")
  public String createGenre(@Valid @ModelAttribute Genre genre) {
    genreService.create(genre);
    return "redirect:/genres";
  }

  @GetMapping("/genres/edit/{genreId}")
  public String editGenre(@PathVariable String genreId, Model model) {
    Genre genre = genreService.findById(genreId);
    model.addAttribute("genre", genre);
    return "genre_edit";
  }

  @PostMapping("/genres/edit")
  public String updateGenre(@Valid @ModelAttribute Genre genre) {
    genreService.update(genre);
    return "redirect:/genres";
  }

  @PostMapping("/genres/delete")
  public String deleteAuthor(@RequestParam("genreId") String genreId) {
    genreService.deleteById(genreId);
    return "redirect:/genres";
  }

  @ExceptionHandler(Exception.class)
  public String handleException(Model model, Exception e) {
    model.addAttribute("message", e.getMessage());
    return "error";
  }
}
