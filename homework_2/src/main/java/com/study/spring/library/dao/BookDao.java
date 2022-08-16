package com.study.spring.library.dao;

import com.study.spring.library.domain.Book;
import java.util.Collection;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookDao extends MongoRepository<Book, String> {

  Optional<Book> findByTitle(String title);
  Collection<Book> findByAuthor(String author);
  Collection<Book> findByGenre(String genre);

}
