package com.study.spring.library.service;

import com.study.spring.library.dao.GenreDao;
import com.study.spring.library.domain.Genre;
import com.study.spring.library.exceptions.EntityNotFoundException;
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
    return genreDao.findAll();
  }

  @Override
  @Transactional
  public Genre create(Genre genre) {
    return genreDao.save(genre);
  }

  @Override
  public Genre findById(Long id) {
    return genreDao.findById(id)
                   .orElseThrow(() -> new EntityNotFoundException("Genre not found", "Genre"));
  }

  @Override
  public Genre findByName(String name) {
    return genreDao.findByName(name)
                   .orElseThrow(() -> new EntityNotFoundException("Genre not found", "Genre"));
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
