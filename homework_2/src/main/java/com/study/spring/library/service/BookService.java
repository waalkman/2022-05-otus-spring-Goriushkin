package com.study.spring.library.service;

import com.study.spring.library.domain.Book;
import java.util.Collection;

public interface BookService {

  Collection<Book> getAll();
  Book create(Book book, String genre, String author);
  Book findById(String id);
  Book findByTitle(String title);
  void update(Book book, String genre, String author);
  void deleteById(String id);

}
