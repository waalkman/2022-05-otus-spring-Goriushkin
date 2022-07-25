package com.study.spring.library.dao;

import com.study.spring.library.domain.Genre;
import java.util.Collection;

public interface GenreDao {

  Collection<Genre> getAll();
  long create(Genre genre);
  Genre getById(Long id);
  Long getIdByName(String name);
  void update(Genre genre);
  void deleteById(Long id);

}
