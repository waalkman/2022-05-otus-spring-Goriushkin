package com.study.spring.library.service;

import com.study.spring.library.domain.Genre;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GenreService {

  Flux<Genre> findAll();
  Mono<Genre> create(Genre genre);
  Mono<Genre> findById(String id);
  Mono<Genre> findByName(String name);
  Mono<Genre> update(Genre genre);
  Mono<Void> deleteById(String id);

}
