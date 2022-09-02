package com.study.spring.library.service;

import com.study.spring.library.dao.BookDao;
import com.study.spring.library.dao.GenreDao;
import com.study.spring.library.domain.Genre;
import com.study.spring.library.exceptions.ConsistencyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

  private final GenreDao genreDao;
  private final BookDao bookDao;

  @Override
  public Flux<Genre> findAll() {
    return genreDao.findAll();
  }

  @Override
  public Mono<Genre> create(Genre genre) {
    return genreDao.existsByName(genre.getName())
                    .flatMap(exists -> {
                      if (exists) {
                        return Mono.error(new ConsistencyException("Cannot create genre. There is another genre with that name in database!"));
                      } else {
                        return genreDao.save(genre);
                      }
                    });
  }

  @Override
  public Mono<Genre> findById(String id) {
    return genreDao.findById(id);
  }

  @Override
  public Mono<Genre> findByName(String name) {
    return genreDao.findByName(name);
  }

  @Override
  public Mono<Genre> update(Genre genre) {
    return genreDao.save(genre);
  }

  @Override
  public Mono<Void> deleteById(String id) {
    return bookDao.existsByGenreId(id)
                  .flatMap(exists -> {
                    if (exists) {
                      return Mono.error(new ConsistencyException("Cannot delete genre. There are books with that genre in database!"));
                    } else {
                      return genreDao.deleteById(id);
                    }
                  });
  }
}
