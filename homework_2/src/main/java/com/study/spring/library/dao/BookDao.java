package com.study.spring.library.dao;

import com.study.spring.library.domain.Book;
import java.util.Collection;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "book")
public interface BookDao extends MongoRepository<Book, String> {

  @RestResource(path = "titles", rel = "title")
  Optional<Book> findByTitle(String title);
  @RestResource(path = "authors", rel = "author")
  Collection<Book> findByAuthor(String author);
  @RestResource(path = "genres", rel = "genre")
  Collection<Book> findByGenre(String genre);

}
