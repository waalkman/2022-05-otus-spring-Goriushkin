package com.study.spring.library.service;

import com.study.spring.library.dao.GenreDao;
import com.study.spring.library.domain.Genre;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

  private final GenreDao genreDao;

  @Override
  public Collection<Genre> getAll() {
    return genreDao.getAll();
  }

  @Override
  @Transactional
  public long create(Genre genre) {
    return genreDao.save(genre);
  }

  @Override
  public Genre getById(Long id) {
    return genreDao.getById(id);
  }

  @Override
  public Genre getByName(String name) {
    return genreDao.getByName(name);
  }

  @Override
  @Transactional
  public void update(Genre genre) {
    genreDao.save(genre);
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    genreDao.deleteById(id);
  }
}
