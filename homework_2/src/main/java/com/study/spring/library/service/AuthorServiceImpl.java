package com.study.spring.library.service;

import com.study.spring.library.dao.AuthorDao;
import com.study.spring.library.domain.Author;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

  private final AuthorDao authorDao;

  @Override
  @Transactional
  public Collection<Author> getAll() {
    return authorDao.getAll();
  }

  @Override
  @Transactional
  public long create(Author author) {
    return authorDao.save(author);
  }

  @Override
  @Transactional(readOnly = true)
  public Author getById(Long id) {
    return authorDao.getById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public Author getByName(String name) {
    return authorDao.getByName(name);
  }

  @Override
  @Transactional
  public void update(Author author) {
    authorDao.save(author);
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    authorDao.deleteById(id);
  }
}
