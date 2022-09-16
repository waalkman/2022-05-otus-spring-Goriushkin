package com.study.spring.library.dao;

import com.study.spring.library.domain.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

public interface BookDao extends MongoRepository<Book, String> {

  @PostAuthorize("hasPermission(returnObject.get(), 'READ') || hasPermission(returnObject.get(), 'ADMINISTRATION')")
  Optional<Book> findByTitle(String title);

  @PostFilter("hasPermission(filterObject, 'READ') || hasPermission(filterObject, 'ADMINISTRATION')")
  List<Book> findAll();

  @PreAuthorize(
      "hasPermission(#id, 'com.study.spring.library.domain.Book', 'DELETE')"
          + "|| hasPermission(#id, 'com.study.spring.library.domain.Book', 'ADMINISTRATION')")
  Optional<Book> findById(@Param("id") String id);

  @PreAuthorize("hasAnyAuthority({'ADMIN', 'MANAGER'}) || hasPermission(#book, 'WRITE') || hasPermission(#book, 'ADMINISTRATION')")
  Book save(@Param("book") Book book);

  @PreAuthorize(
      "hasPermission(#id, 'com.study.spring.library.domain.Book', 'DELETE')"
          + "|| hasPermission(#id, 'com.study.spring.library.domain.Book', 'ADMINISTRATION')")
  void deleteById(@Param("id") String id);


  boolean existsByAuthorId(String authorId);
  boolean existsByGenreId(String genreId);
  boolean existsByTitle(String title);

}
