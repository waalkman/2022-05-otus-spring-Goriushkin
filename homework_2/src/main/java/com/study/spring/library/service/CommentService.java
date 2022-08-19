package com.study.spring.library.service;

import com.study.spring.library.domain.Comment;
import java.util.Collection;

public interface CommentService {

  void create(Comment comment, String bookTitle);
  Comment findById(String bookTitle, String id);
  Collection<Comment> findByBookTitle(String bookTitle);
  void update(Comment comment, String bookTitle);
  void deleteById(String bookTitle, String id);

}
