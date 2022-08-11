package com.study.spring.library.service;

import com.study.spring.library.dao.AuthorDao;
import com.study.spring.library.domain.Author;
import com.study.spring.library.exceptions.EntityNotFoundException;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

  private final AuthorDao authorDao;

  @Override
  public Collection<Author> getAll() {
    return authorDao.findAll();
  }

  @Override
  public Author create(Author author) {
    return authorDao.save(author);
  }

  @Override
  public Author findById(Long id) {
    return authorDao.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Author not found", "Author"));
  }

  @Override
  public Author findByName(String name) {
    return authorDao.findByName(name)
                    .orElseThrow(() -> new EntityNotFoundException("Author not found", "Author"));
  }

  @Override
  public void update(Author author) {
    authorDao.save(author);
  }

  @Override
  public void deleteById(Long id) {
    authorDao.deleteById(id);
  }
}
