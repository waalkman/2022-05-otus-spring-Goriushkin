package com.study.spring.library.service;

import com.study.spring.library.domain.Genre;
import java.util.Collection;

public interface GenreService {

  Collection<Genre> getAll();
  Genre create(Genre genre);
  Genre findById(Long id);
  Genre findByName(String name);
  void update(Genre genre);
  void deleteById(Long id);

}
