package com.study.spring.library.service;

import com.study.spring.library.domain.Book;
import com.study.spring.library.dto.CommentedBook;
import java.util.Collection;

public interface BookService {

  Collection<Book> getAll();
  Book create(Book book, String genre, String author);
  CommentedBook findById(Long id);
  CommentedBook findByTitle(String title);
  void update(Book book, String genre, String author);
  void deleteById(Long id);

}
