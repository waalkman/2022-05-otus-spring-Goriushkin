package com.study.spring.library.service;

import com.study.spring.library.domain.Author;
import java.util.Collection;
import org.springframework.transaction.annotation.Transactional;

public interface AuthorService {

  @Transactional(readOnly = true)
  Collection<Author> getAll();
  long create(Author author);
  Author getById(Long id);
  Author getByName(String name);
  void update(Author author);
  void deleteById(Long id);

}
