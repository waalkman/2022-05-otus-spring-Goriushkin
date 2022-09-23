package com.study.spring.batch.mongo.dao;

import com.study.spring.batch.mongo.model.Author;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

public interface AuthorDao extends MongoRepository<Author, String> {

  List<Author> findAll();
  Optional<Author> findById(@Param("id") String id);
  Author save(@Param("author") Author author);
  void deleteById(@Param("id") String id);

}
