package com.study.spring.library.controller;

import com.study.spring.library.domain.Author;
import com.study.spring.library.domain.Book;
import com.study.spring.library.domain.Genre;
import com.study.spring.library.dto.BookDto;
import com.study.spring.library.service.AuthorService;
import com.study.spring.library.service.BookService;
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
public class BookController {

  private final BookService bookService;
  private final AuthorService authorService;
  private final GenreService genreService;

  @GetMapping("/books")
  public String listBooks(Model model) {
    Collection<Book> books = bookService.getAll();
    Collection<Author> authors = authorService.getAll();
    Collection<Genre> genres = genreService.getAll();

    model.addAttribute("authors", authors);
    model.addAttribute("genres", genres);
    model.addAttribute("books", books);
    return "books";
  }

  @GetMapping("/books/{bookId}")
  public String showBook(@PathVariable String bookId, Model model) {
    Book book = bookService.findById(bookId);
    model.addAttribute("book", book);
    return "book";
  }

  @PostMapping("/books/delete")
  public String deleteBook(@RequestParam("bookId") String bookId) {
    bookService.deleteById(bookId);
    return "redirect:/books";
  }

  @GetMapping("/books/edit/{bookId}")
  public String editBookPage(@PathVariable String bookId, Model model) {
    Book book = bookService.findById(bookId);
    Collection<Author> authors = authorService.getAll();
    Collection<Genre> genres = genreService.getAll();

    model.addAttribute("book", book);
    model.addAttribute("authors", authors);
    model.addAttribute("genres", genres);
    return "book_edit";
  }

  @PostMapping("/books/edit")
  public String updateBook(@Valid @ModelAttribute BookDto book) {
    bookService.update(book.toBook(), book.getGenre(), book.getAuthor());
    return "redirect:/books/" + book.getId();
  }

  @PostMapping("/books")
  public String createBookPage(@Valid @ModelAttribute BookDto book) {
    bookService.create(book.toBook(), book.getGenre(), book.getAuthor());
    return "redirect:/books";
  }

  @ExceptionHandler(Exception.class)
  public String handleException(Model model, Exception e) {
    model.addAttribute("message", e.getMessage());
    return "error";
  }
}
