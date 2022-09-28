package com.study.spring.library.dao;

import com.study.spring.library.domain.Author;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "author")
public interface AuthorDao extends MongoRepository<Author, String> {

  @RestResource(path = "names", rel = "name")
  Optional<Author> findByName(String name);

}
