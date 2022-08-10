package com.study.spring.library.service;

import com.study.spring.library.domain.Book;
import com.study.spring.library.dto.CommentedBook;
import java.util.Collection;

public interface BookService {

  Collection<Book> getAll();
  long create(Book book, String genre, String author);
  CommentedBook getById(Long id);
  CommentedBook getByTitle(String title);
  void update(Book book, String genre, String author);
  void deleteById(Long id);

}
