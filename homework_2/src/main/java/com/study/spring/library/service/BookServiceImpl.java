package com.study.spring.library.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.study.spring.library.dao.AuthorDao;
import com.study.spring.library.dao.BookDao;
import com.study.spring.library.dao.GenreDao;
import com.study.spring.library.domain.Author;
import com.study.spring.library.domain.Book;
import com.study.spring.library.domain.Genre;
import com.study.spring.library.exceptions.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

  private final BookDao bookDao;
  private final AuthorDao authorDao;
  private final GenreDao genreDao;

  @Override
  @HystrixCommand(groupKey = "BookService", fallbackMethod = "getBooksFallback")
  public Collection<Book> getAll() {
    return bookDao.findAll();
  }

  @Override
  @HystrixCommand(groupKey = "BookService")
  public Book create(Book book, String genre, String author) {
    checkAuthorAndGenre(book, genre, author);
    book.setComments(new ArrayList<>());
    return bookDao.save(book);
  }

  @Override
  @HystrixCommand(groupKey = "BookService", fallbackMethod = "fallbackBook")
  public Book findById(String id) {
    return bookDao.findById(id)
                  .orElseThrow(() -> new EntityNotFoundException("Book not found", "Book"));
  }

  @Override
  @HystrixCommand(groupKey = "BookService", fallbackMethod = "fallbackBook")
  public Book findByTitle(String title) {
    return bookDao.findByTitle(title)
                  .orElseThrow(() -> new EntityNotFoundException("Book not found", "Book"));
  }

  @Override
  @HystrixCommand(groupKey = "BookService")
  public void update(Book book, String genre, String author) {
    Book currentBook = bookDao.findById(book.getId())
                              .orElseThrow(() -> new EntityNotFoundException("Book not found", "Book"));

    checkAuthorAndGenre(book, genre, author);
    currentBook.setTitle(book.getTitle());
    currentBook.setDescription(book.getDescription());
    currentBook.setAuthor(book.getAuthor());
    currentBook.setGenre(book.getGenre());

    bookDao.save(currentBook);
  }

  @Override
  @HystrixCommand(groupKey = "BookService")
  public void deleteById(String id) {
    bookDao.deleteById(id);
  }

  private void checkAuthorAndGenre(Book book, String genre, String author) {
    book.setGenre(genreDao.findByName(genre).orElseThrow(() -> new EntityNotFoundException("Genre not found", "Genre")));
    book.setAuthor(authorDao.findByName(author).orElseThrow(() -> new EntityNotFoundException("Author not found", "Author")));
  }

  private Collection<Book> getBooksFallback() {
    return Collections.singletonList(fallbackBook("Id or title"));
  }

  private Book fallbackBook(String idOrTitle) {
    return Book.builder()
               .id(idOrTitle)
               .title(idOrTitle)
               .description("Fallback description")
               .genre(Genre.builder().name("Fallback genre").build())
               .author(Author.builder().name("Fallback author").build())
               .build();
  }
}
