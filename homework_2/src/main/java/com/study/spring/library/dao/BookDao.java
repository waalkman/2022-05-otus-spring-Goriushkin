package com.study.spring.library.dao;

import com.study.spring.library.domain.Book;
import java.util.Collection;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.access.prepost.PostAuthorize;

public interface BookDao extends MongoRepository<Book, String> {

  @PostAuthorize("hasPermission(returnObject, 'READ')")
  Optional<Book> findByTitle(String title);
  boolean existsByAuthorId(String authorId);
  boolean existsByGenreId(String genreId);
  boolean existsByTitle(String title);

}
