package com.study.spring.batch.postgre.dao;

import com.study.spring.batch.postgre.domain.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookDaoPG extends JpaRepository<Book, Long> {

  @Override
  @EntityGraph(attributePaths = {"genre", "author"})
  List<Book> findAll();

  Optional<Book> findByMongoId(String mongoId);

}