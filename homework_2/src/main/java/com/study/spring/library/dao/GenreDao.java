package com.study.spring.library.dao;

import com.study.spring.library.domain.Genre;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;

public interface GenreDao extends MongoRepository<Genre, String> {

  @PostAuthorize("hasPermission(returnObject, 'READ')")
  Optional<Genre> findByName(String name);

  @PostFilter("hasPermission(filterObject, 'READ')")
  List<Genre> findAll();

  boolean existsByName(String name);

}
