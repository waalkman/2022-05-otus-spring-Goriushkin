package com.study.spring.batch.mongo.dao;

import com.study.spring.batch.mongo.model.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

public interface BookDao extends MongoRepository<Book, String> {

  List<Book> findAll();
  Optional<Book> findById(@Param("id") String id);
  Book save(@Param("book") Book book);
  void deleteById(@Param("id") String id);

}
