package com.study.spring.library.controller;

import com.study.spring.library.domain.Book;
import com.study.spring.library.dto.BookDto;
import com.study.spring.library.service.BookService;
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
public class BookController {

  private final BookService bookService;

  @GetMapping("/api/v1/books")
  public Collection<Book> listBooks() {
    return bookService.getAll();
  }

  @PostMapping("/api/v1/books")
  public Book createBook(@Valid @RequestBody BookDto bookDto) {
    return bookService.create(bookDto.toBook(), bookDto.getGenre(), bookDto.getAuthor());
  }

  @GetMapping("/api/v1/books/{bookId}")
  public Book getBook(@PathVariable String bookId) {
    return bookService.findById(bookId);
  }

  @DeleteMapping("/api/v1/books/{id}")
  public void deleteBook(@PathVariable("id") String bookId) {
    bookService.deleteById(bookId);
  }

  @PatchMapping("/api/v1/books")
  public void updateBook(@Valid @RequestBody BookDto book) {
    bookService.update(book.toBook(), book.getGenre(), book.getAuthor());
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public String handleException(Exception e) {
    return e.getMessage();
  }
}
