package com.study.spring.library.service;

import com.study.spring.library.domain.Author;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AuthorService {

  Flux<Author> findAll();
  Mono<Author> create(Author author);
  Mono<Author> findById(String id);
  Mono<Author> findByName(String name);
  Mono<Author> update(Author author);
  Mono<Void> deleteById(String id);

}
