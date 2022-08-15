package com.study.spring.library.service;

import com.study.spring.library.domain.Comment;
import java.util.Collection;

public interface CommentService {

  Collection<Comment> getAll();
  Comment create(Comment comment, String bookTitle);
  Comment findById(Long id);
  void update(Comment comment, String bookTitle);
  void deleteById(Long id);

}
