package com.study.spring.library.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.study.spring.library.dao.AuthorDao;
import com.study.spring.library.domain.Author;
import com.study.spring.library.exceptions.EntityNotFoundException;
import java.util.Collection;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

  private final AuthorDao authorDao;

  @Override
  @HystrixCommand(groupKey = "AuthorService", fallbackMethod = "getAuthorsFallback")
  public Collection<Author> getAll() {
    return authorDao.findAll();
  }

  @Override
  @HystrixCommand(groupKey = "AuthorService")
  public Author create(Author author) {
    return authorDao.save(author);
  }

  @Override
  @HystrixCommand(groupKey = "AuthorService", fallbackMethod = "fallbackAuthor")
  public Author findById(String id) {
    return authorDao.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Author not found", "Author"));
  }

  @Override
  @HystrixCommand(groupKey = "AuthorService", fallbackMethod = "fallbackAuthor")
  public Author findByName(String name) {
    return authorDao.findByName(name)
                    .orElseThrow(() -> new EntityNotFoundException("Author not found", "Author"));
  }

  @Override
  @HystrixCommand(groupKey = "AuthorService")
  public void update(Author author) {
    authorDao.save(author);
  }

  @Override
  @HystrixCommand(groupKey = "AuthorService")
  public void deleteById(String id) {
    authorDao.deleteById(id);
  }

  private Collection<Author> getAuthorsFallback() {
    return Collections.singletonList(fallbackAuthor("Id or name"));
  }

  private Author fallbackAuthor(String idOrName) {
    return Author.builder().id(idOrName).name("Fallback Author").build();
  }

}
