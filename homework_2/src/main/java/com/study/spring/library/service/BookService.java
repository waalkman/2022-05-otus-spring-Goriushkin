package com.study.spring.library.service;

import com.study.spring.library.domain.Book;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookService {

  Flux<Book> findAll();
  Mono<Book> create(Book book, String genre, String author);
  Mono<Book> findById(String id);
  Mono<Book> findByTitle(String title);
  Mono<Book> update(Book book, String genre, String author);
  Mono<Void> deleteById(String id);

}
