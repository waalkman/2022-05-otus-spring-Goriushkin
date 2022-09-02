package com.study.spring.library.dao;

import com.study.spring.library.domain.Book;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookDao extends ReactiveMongoRepository<Book, String> {

  Mono<Book> findByTitle(String title);
  Flux<Book> findAllByAuthor(String author);
  Flux<Book> findAllByGenre(String genre);
  Mono<Boolean> existsByAuthorId(String authorId);
  Mono<Boolean> existsByGenreId(String genreId);

}
