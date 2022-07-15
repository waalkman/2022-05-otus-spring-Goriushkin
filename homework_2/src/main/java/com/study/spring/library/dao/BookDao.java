package com.study.spring.library.dao;

import com.study.spring.library.domain.Book;
import java.util.Collection;

public interface BookDao {

  Collection<Book> getAll();
  long create(Book book);
  Book getById(Long id);
  void update(Book book);
  void deleteById(Long id);

}
