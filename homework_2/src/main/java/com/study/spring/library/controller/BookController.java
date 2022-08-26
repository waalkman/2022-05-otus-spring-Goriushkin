package com.study.spring.library.controller;

import com.study.spring.library.domain.Book;
import com.study.spring.library.dto.BookDto;
import com.study.spring.library.service.BookService;
import java.util.Collection;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
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

//  @GetMapping("/books/{bookId}")
//  public String showBbook(@PathVariable String bookId, Model model) {
//    Book book = bookService.findById(bookId);
//    model.addAttribute("book", book);
//    return "book";
//  }
//
//  @PostMapping("/books/delete")
//  public String deleteBook(@RequestParam("bookId") String bookId) {
//    bookService.deleteById(bookId);
//    return "redirect:/books";
//  }
//
//  @GetMapping("/books/edit/{bookId}")
//  public String editBookPage(@PathVariable String bookId, Model model) {
//    Book book = bookService.findById(bookId);
//    Collection<Author> authors = authorService.getAll();
//    Collection<Genre> genres = genreService.getAll();
//
//    model.addAttribute("book", book);
//    model.addAttribute("authors", authors);
//    model.addAttribute("genres", genres);
//    return "book_edit";
//  }
//
//  @PostMapping("/books/edit")
//  public String updateBook(@Valid @ModelAttribute BookDto book) {
//    bookService.update(book.toBook(), book.getGenre(), book.getAuthor());
//    return "redirect:/books/" + book.getId();
//  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public String handleException(Exception e) {
    return e.getMessage();
  }
}
