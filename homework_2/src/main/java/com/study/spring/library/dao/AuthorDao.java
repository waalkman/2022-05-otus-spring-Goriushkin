package com.study.spring.library.dao;

import com.study.spring.library.domain.Author;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

public interface AuthorDao extends MongoRepository<Author, String> {

  @PostAuthorize("hasPermission(returnObject, 'READ')")
  Optional<Author> findByName(String name);

  @PostFilter("hasPermission(filterObject, 'READ')")
  List<Author> findAll();

  @PostAuthorize("hasPermission(returnObject, 'READ')")
  Optional<Author> findById(String id);

  @PreAuthorize("hasPermission(#author, 'WRITE')")
  Author save(@Param("author") Author author);

  @PreAuthorize("hasPermission(#id, 'com.study.spring.library.domain.Author', 'DELETE')")
  void deleteById(@Param("id") String id);

  boolean existsByName(String name);

}
