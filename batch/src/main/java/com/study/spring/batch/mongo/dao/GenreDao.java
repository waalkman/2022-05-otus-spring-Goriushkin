package com.study.spring.batch.mongo.dao;

import com.study.spring.batch.mongo.model.Genre;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

public interface GenreDao extends MongoRepository<Genre, String> {

  List<Genre> findAll();
  Optional<Genre> findById(@Param("id") String id);
  Genre save(@Param("genre") Genre genre);
  void deleteById(@Param("id") String id);

}
