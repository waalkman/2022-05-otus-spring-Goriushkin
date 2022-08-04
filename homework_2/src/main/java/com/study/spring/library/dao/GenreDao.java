package com.study.spring.library.dao;

import com.study.spring.library.domain.Genre;
import java.util.Collection;

public interface GenreDao {

  Collection<Genre> getAll();
  long save(Genre genre);
  Genre getById(Long id);
  Genre getByName(String name);
  void deleteById(Long id);

}
