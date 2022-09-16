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

  @PostAuthorize("hasPermission(returnObject.get(), 'READ') || hasPermission(returnObject.get(), 'ADMINISTRATION')")
  Optional<Author> findByName(String name);

  @PostFilter("hasPermission(filterObject, 'READ') || hasPermission(filterObject, 'ADMINISTRATION')")
  List<Author> findAll();

  @PreAuthorize(
      "hasPermission(#id, 'com.study.spring.library.domain.Author', 'DELETE')"
          + "|| hasPermission(#id, 'com.study.spring.library.domain.Author', 'ADMINISTRATION')")
  Optional<Author> findById(@Param("id") String id);

  @PreAuthorize("hasAnyAuthority({'ADMIN', 'MANAGER'}) || hasPermission(#author, 'WRITE') || hasPermission(#author, 'ADMINISTRATION')")
  Author save(@Param("author") Author author);

  @PreAuthorize(
      "hasPermission(#id, 'com.study.spring.library.domain.Author', 'DELETE')"
          + "|| hasPermission(#id, 'com.study.spring.library.domain.Author', 'ADMINISTRATION')")
  void deleteById(@Param("id") String id);

  boolean existsByName(String name);

}
