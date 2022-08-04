package com.study.spring.library.service;

import com.study.spring.library.domain.Genre;
import java.util.Collection;

public interface GenreService {

  Collection<Genre> getAll();
  long create(Genre genre);
  Genre getById(Long id);
  Genre getByName(String name);
  void update(Genre genre);
  void deleteById(Long id);

}
