package com.study.spring.library.service;

import com.study.spring.library.dao.AuthorDao;
import com.study.spring.library.dao.BookDao;
import com.study.spring.library.dao.GenreDao;
import com.study.spring.library.domain.Book;
import com.study.spring.library.exceptions.EntityNotFoundException;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

  private final BookDao bookDao;
  private final AuthorDao authorDao;
  private final GenreDao genreDao;

  @Override
  public Flux<Book> findAll() {
    return bookDao.findAll();
  }

  @Override
  public Mono<Book> create(Book book, String genre, String author) {
    book.setComments(new ArrayList<>());
    return checkAuthorAndGenre(book, genre, author)
        .then(bookDao.save(book));
  }

  @Override
  public Mono<Book> findById(String id) {
    return bookDao.findById(id);
  }

  @Override
  public Mono<Book> findByTitle(String title) {
    return bookDao.findByTitle(title);
  }

  @Override
  public Mono<Book> update(Book book, String genre, String author) {
    return checkAuthorAndGenre(book, genre, author)
        .then(bookDao.save(book));
  }

  @Override
  public Mono<Void> deleteById(String id) {
    return bookDao.deleteById(id);
  }

  private Mono<Void> checkAuthorAndGenre(Book book, String genreName, String authorName) {
    return genreDao.findByName(genreName)
                   .switchIfEmpty(Mono.error(new EntityNotFoundException("Genre not found", "Genre")))
                   .doOnNext(book::setGenre)
                   .then(authorDao.findByName(authorName)
                                 .switchIfEmpty(Mono.error(new EntityNotFoundException("Author not found", "Author")))
                                 .doOnNext(book::setAuthor))
                   .then();
  }
}
