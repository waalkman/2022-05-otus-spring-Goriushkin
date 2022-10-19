package com.study.spring.library.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.study.spring.library.dao.GenreDao;
import com.study.spring.library.domain.Genre;
import com.study.spring.library.exceptions.EntityNotFoundException;
import java.util.Collection;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

  private final GenreDao genreDao;

  @Override
  @HystrixCommand(groupKey = "GenreService", fallbackMethod = "getGenresFallback")
  public Collection<Genre> getAll() {
    return genreDao.findAll();
  }

  @Override
  @HystrixCommand(groupKey = "GenreService")
  public Genre create(Genre genre) {
    return genreDao.save(genre);
  }

  @Override
  @HystrixCommand(groupKey = "GenreService", fallbackMethod = "fallbackGenre")
  public Genre findById(String id) {
    return genreDao.findById(id)
                   .orElseThrow(() -> new EntityNotFoundException("Genre not found", "Genre"));
  }

  @Override
  @HystrixCommand(groupKey = "GenreService", fallbackMethod = "fallbackGenre")
  public Genre findByName(String name) {
    return genreDao.findByName(name)
                   .orElseThrow(() -> new EntityNotFoundException("Genre not found", "Genre"));
  }

  @Override
  @HystrixCommand(groupKey = "GenreService")
  public void update(Genre genre) {
    genreDao.save(genre);
  }

  @Override
  @HystrixCommand(groupKey = "GenreService")
  public void deleteById(String id) {
    genreDao.deleteById(id);
  }

  private Collection<Genre> getGenresFallback() {
    return Collections.singletonList(fallbackGenre("Id or name"));
  }

  private Genre fallbackGenre(String idOrName) {
    return Genre.builder().id(idOrName).name("Fallback genre").build();
  }
}
