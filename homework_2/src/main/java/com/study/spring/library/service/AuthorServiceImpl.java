package com.study.spring.library.service;

import com.study.spring.library.dao.AuthorDao;
import com.study.spring.library.dao.BookDao;
import com.study.spring.library.domain.Author;
import com.study.spring.library.exceptions.ConsistencyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

  private final AuthorDao authorDao;
  private final BookDao bookDao;

  @Override
  public Flux<Author> findAll() {
    return authorDao.findAll();
  }

  @Override
  public Mono<Author> create(Author author) {
    return authorDao.existsByName(author.getName())
        .flatMap(exists -> {
          if (exists) {
            return Mono.error(new ConsistencyException("Cannot create author. There is another author with that name in database!"));
          } else {
            return authorDao.save(author);
          }
        });
  }

  @Override
  public Mono<Author> findById(String id) {
    return authorDao.findById(id);
  }

  @Override
  public Mono<Author> findByName(String name) {
    return authorDao.findByName(name);
  }

  @Override
  public Mono<Author> update(Author author) {
    return authorDao.save(author);
  }

  @Override
  public Mono<Void> deleteById(String id) {
    return bookDao.existsByAuthorId(id)
        .flatMap(exists -> {
          if (exists) {
            return Mono.error(new ConsistencyException("Cannot delete author. There are book(s) with that author in database!"));
          } else {
            return authorDao.deleteById(id);
          }
        });
  }
}
