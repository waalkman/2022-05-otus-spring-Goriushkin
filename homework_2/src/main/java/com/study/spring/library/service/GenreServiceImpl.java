package com.study.spring.library.service;

import com.study.spring.library.dao.BookDao;
import com.study.spring.library.dao.GenreDao;
import com.study.spring.library.domain.Genre;
import com.study.spring.library.exceptions.ConsistencyException;
import com.study.spring.library.exceptions.EntityNotFoundException;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

  private final BookDao bookDao;
  private final GenreDao genreDao;

  @Override
  public Collection<Genre> getAll() {
    return genreDao.findAll();
  }

  @Override
  public Genre create(Genre genre) {
    return save(genre);
  }

  @Override
  public Genre findById(String id) {
    return genreDao.findById(id)
                   .orElseThrow(() -> new EntityNotFoundException("Genre not found", "Genre"));
  }

  @Override
  public Genre findByName(String name) {
    return genreDao.findByName(name)
                   .orElseThrow(() -> new EntityNotFoundException("Genre not found", "Genre"));
  }

  @Override
  public void update(Genre genre) {
    save(genre);
  }

  private Genre save(Genre genre) {
    if (genreDao.existsByName(genre.getName())) {
      throw new ConsistencyException("Cannot create genre. There is another genre with that name in database!");
    } else {
      return genreDao.save(genre);
    }
  }

  @Override
  public void deleteById(String id) {
    if (bookDao.existsByGenreId(id)) {
      throw new ConsistencyException("Cannot delete genre. There are books with that genre in database!");
    } else {
      genreDao.deleteById(id);
    }
  }
}
