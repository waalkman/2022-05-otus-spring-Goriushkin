package com.study.spring.library.service;

import com.study.spring.library.domain.Book;
import com.study.spring.library.domain.Comment;
import java.util.Collection;
import reactor.core.publisher.Mono;

public interface CommentService {

  Mono<Book> create(Comment comment, String bookId);
  Comment findById(String bookId, String id);
  Collection<Comment> findByBook(String bookId);
  Mono<Book> update(Comment comment, String bookId);
  Mono<Book> deleteById(String bookId, String id);

}
