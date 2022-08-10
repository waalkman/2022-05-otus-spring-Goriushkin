package com.study.spring.library.dao;

import com.study.spring.library.domain.Author;
import java.util.Collection;

public interface AuthorDao {

  Collection<Author> getAll();
  Author getById(Long id);
  Author getByName(String name);
  void deleteById(Long id);
  long save(Author author);

}
