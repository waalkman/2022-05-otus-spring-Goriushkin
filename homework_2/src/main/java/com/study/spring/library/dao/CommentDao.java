package com.study.spring.library.dao;

import com.study.spring.library.domain.Comment;
import java.util.Collection;

public interface CommentDao {

  Collection<Comment> getAll();
  long save(Comment comment);
  Comment getById(Long id);
  void deleteById(Long id);

}
