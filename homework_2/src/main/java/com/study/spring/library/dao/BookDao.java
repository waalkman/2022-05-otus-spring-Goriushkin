package com.study.spring.library.dao;

import com.study.spring.library.domain.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookDao extends JpaRepository<Book, Long> {

  Optional<Book> findByTitle(String title);

  @Override
  @EntityGraph(attributePaths = {"genre", "author"})
  List<Book> findAll();

}
