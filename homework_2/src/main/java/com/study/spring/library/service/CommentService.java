package com.study.spring.library.service;

import com.study.spring.library.domain.Comment;
import java.util.Collection;

public interface CommentService {

  void create(Comment comment, String bookId);
  Comment findById(String bookId, String id);
  Collection<Comment> findByBook(String bookId);
  void update(Comment comment, String bookId);
  void deleteById(String bookId, String id);

}
