package com.study.spring.library.dao;

import com.study.spring.library.domain.Book;
import java.util.Collection;

public interface BookDao {

  Collection<Book> getAll();
  long save(Book book);
  Book getById(Long id);
  Book getByTitle(String title);
  void deleteById(Long id);

}
