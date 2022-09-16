package com.study.spring.library.dao;

import com.study.spring.library.domain.Genre;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

public interface GenreDao extends MongoRepository<Genre, String> {

  @PostAuthorize("hasPermission(returnObject.get(), 'READ') || hasPermission(returnObject.get(), 'ADMINISTRATION')")
  Optional<Genre> findByName(String name);

  @PostFilter("hasPermission(filterObject, 'READ') || hasPermission(filterObject, 'ADMINISTRATION')")
  List<Genre> findAll();

  @PreAuthorize(
      "hasPermission(#id, 'com.study.spring.library.domain.Genre', 'DELETE')"
          + "|| hasPermission(#id, 'com.study.spring.library.domain.Genre', 'ADMINISTRATION')")
  Optional<Genre> findById(@Param("id") String id);

  @PreAuthorize("hasAnyAuthority({'ADMIN', 'MANAGER'}) || hasPermission(#genre, 'WRITE') || hasPermission(#genre, 'ADMINISTRATION')")
  Genre save(@Param("genre") Genre genre);

  @PreAuthorize(
      "hasPermission(#id, 'com.study.spring.library.domain.Genre', 'DELETE')"
          + "|| hasPermission(#id, 'com.study.spring.library.domain.Genre', 'ADMINISTRATION')")
  void deleteById(@Param("id") String id);

  boolean existsByName(String name);

}
