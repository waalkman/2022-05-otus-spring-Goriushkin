package com.study.spring.library.service;

import com.study.spring.library.domain.Author;
import java.util.Collection;

public interface AuthorService {

  Collection<Author> getAll();
  Author create(Author author);
  Author findById(String id);
  Author findByName(String name);
  void update(Author author);
  void deleteById(String id);

}
