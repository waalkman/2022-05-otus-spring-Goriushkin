package com.study.spring.library.dao;

import com.study.spring.library.domain.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentDao extends JpaRepository<Comment, Long> {

  @Override
  @EntityGraph(attributePaths = {"book"})
  List<Comment> findAll();

}
