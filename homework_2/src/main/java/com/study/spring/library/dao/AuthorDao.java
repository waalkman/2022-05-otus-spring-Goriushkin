package com.study.spring.library.dao;

import com.study.spring.library.domain.Author;
import java.util.Collection;

public interface AuthorDao {

  Collection<Author> getAll();
  long create(Author author);
  Author getById(Long id);
  Long getIdByName(String name);
  void update(Author author);
  void deleteById(Long id);

}
