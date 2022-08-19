package com.study.spring.library.dao;

import com.study.spring.library.domain.Genre;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GenreDao extends MongoRepository<Genre, String> {

  Optional<Genre> findByName(String name);

}
