package com.study.spring.library.dao;

import com.study.spring.library.domain.Comment;
import java.util.Collection;

public interface CommentDao {

  Collection<Comment> getAll();
  long create(Comment comment);
  Comment getById(Long id);
  void update(Comment comment);
  void deleteById(Long id);

}
