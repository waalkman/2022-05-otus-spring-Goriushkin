package com.study.spring.library.dao;

import com.study.spring.library.domain.Book;
import java.util.Collection;

public interface BookDao {

  Collection<Book> getAll();
  long create(Book book, String genre, String author);
  Book getById(Long id);
  Book getByTitle(String title);
  void update(Book book, String genre, String author);
  void deleteById(Long id);

}
