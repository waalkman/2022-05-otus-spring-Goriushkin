package com.study.spring.library.dao;

import com.study.spring.library.domain.Genre;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface GenreDao extends ReactiveMongoRepository<Genre, String> {

  Mono<Genre> findByName(String name);
  Mono<Boolean> existsByName(String name);

}
