package com.study.spring.library.dao;

import com.study.spring.library.domain.Author;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthorDao extends MongoRepository<Author, String> {

  Optional<Author> findByName(String name);

}
