package com.study.spring.library.dao;

import com.study.spring.library.domain.Genre;
import com.study.spring.library.exceptions.EntityNotFoundException;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GenreDaoImpl implements GenreDao {

  @PersistenceContext
  private final EntityManager em;

  @Override
  @Transactional(readOnly = true)
  public Collection<Genre> getAll() {
    TypedQuery<Genre> genreQuery = em.createQuery("select g from Genre g", Genre.class);
    return genreQuery.getResultList();
  }

  @Override
  @Transactional
  public long create(Genre genre) {
    return save(genre);
  }

  @Override
  @Transactional(readOnly = true)
  public Genre getById(Long id) {
    Genre genre = em.find(Genre.class, id);
    if (genre == null) {
      throw new EntityNotFoundException("Genre not found", "Genre");
    } else {
      return genre;
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Genre getByName(String name) {
    TypedQuery<Genre> genreQuery = em.createQuery("select g from Genre g where g.name = :name", Genre.class);
    genreQuery.setParameter("name", name);

    List<Genre> foundGenres = genreQuery.getResultList();
    if (foundGenres.size() == 1) {
      return foundGenres.iterator().next();
    } else {
      throw new EntityNotFoundException("Genre not found", "Genre");
    }
  }

  @Override
  @Transactional
  public void update(Genre genre) {
    save(genre);
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    Genre genre = getById(id);
    em.remove(genre);
  }

  private long save(Genre genre) {
    if (genre.getId() == null) {
      em.persist(genre);
    } else if (em.find(Genre.class, genre.getId()) != null) {
      em.merge(genre);
    } else {
      throw new EntityNotFoundException("Genre not found", "Genre");
    }
    return genre.getId();
  }
}
