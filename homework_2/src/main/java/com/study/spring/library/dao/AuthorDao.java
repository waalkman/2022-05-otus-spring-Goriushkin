package com.study.spring.library.dao;

import com.study.spring.library.domain.Author;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface AuthorDao extends ReactiveMongoRepository<Author, String> {

  Mono<Author> findByName(String name);
  Mono<Boolean> existsByName(String name);

}
