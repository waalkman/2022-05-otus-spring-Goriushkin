package com.study.spring.library.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.study.spring.library.domain.Genre;
import com.study.spring.library.exceptions.EntityNotFoundException;
import javax.persistence.PersistenceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(GenreDaoImpl.class)
class GenreDaoImplTest {

  @Autowired
  private GenreDaoImpl genreDao;
  @Autowired
  private TestEntityManager em;

  @Test
  void getAll() {
    int expectedAuthorsAmount = 4;
    int actualAuthorsAmount = genreDao.getAll().size();
    assertEquals(expectedAuthorsAmount, actualAuthorsAmount);
  }

  @Test
  void create_success() {
    String name = "some name";
    Genre genre = Genre.builder().name(name).build();
    long id = genreDao.save(genre);
    Genre createdGenre = em.find(Genre.class, id);
    assertEquals(genre, createdGenre);
  }

  @Test
  void create_tooLongName_success() {
    String name = "some name that is veeeeeery looooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong";
    Genre genre = Genre.builder().name(name).build();
    assertThrows(PersistenceException.class, () -> genreDao.save(genre));
  }

  @Test
  void getById_success() {
    Genre expectedGenre = DaoTestUtils.createGenre(em);
    Long id = expectedGenre.getId();
    Genre actualGenre = genreDao.getById(id);
    assertEquals(expectedGenre, actualGenre);
  }

  @Test
  void getById_idNotExists_success() {
    assertThrows(EntityNotFoundException.class, () -> genreDao.getById(6666L));
  }

  @Test
  void getByName_success() {
    Genre createdGenre = DaoTestUtils.createGenre(em);
    String name = createdGenre.getName();
    Genre genre = genreDao.getByName(name);
    assertEquals(createdGenre, genre);
  }

  @Test
  void update_success() {
    String newName = "Updated name";
    Genre createdGenre = DaoTestUtils.createGenre(em);
    em.detach(createdGenre);
    createdGenre.setName(newName);

    genreDao.save(createdGenre);
    Genre updatedGenre = em.find(Genre.class, createdGenre.getId());
    assertEquals(newName, updatedGenre.getName());
  }

  @Test
  void deleteById_success() {
    Genre createdGenre = DaoTestUtils.createGenre(em);
    Long id = createdGenre.getId();
    em.detach(createdGenre);

    genreDao.deleteById(id);

    assertNull(em.find(Genre.class, id));
  }

}