package com.study.spring.library.dao;

import com.study.spring.library.domain.Genre;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "genre")
public interface GenreDao extends MongoRepository<Genre, String> {

  @RestResource(path = "names", rel = "name")
  Optional<Genre> findByName(String name);

}
