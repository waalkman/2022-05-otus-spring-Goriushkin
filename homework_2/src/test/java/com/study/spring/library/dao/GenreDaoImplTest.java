package com.study.spring.library.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.study.spring.library.domain.Genre;
import com.study.spring.library.exceptions.DataQueryException;
import com.study.spring.library.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

@JdbcTest
@Import(GenreDaoImpl.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GenreDaoImplTest {

  @Autowired
  private GenreDaoImpl genreDao;

  @Test
  void getAll_success() {
    int expectedAuthorsAmount = 4;
    int actualAuthorsAmount = genreDao.getAll().size();
    assertEquals(expectedAuthorsAmount, actualAuthorsAmount);
  }

  @Test
  void create_success() {
    String name = "some genre";
    Genre genre = Genre.builder().name(name).build();
    long id = genreDao.create(genre);
    Genre createdGenre = genreDao.getById(id);
    assertEquals(genre.getName(), createdGenre.getName());
  }


  @Test
  void create_tooLongName_success() {
    String name = "some name that is veeeeeery looooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong";
    Genre genre = Genre.builder().name(name).build();
    assertThrows(DataQueryException.class, () -> genreDao.create(genre));
  }

  @Test
  void getById_success() {
    Genre expectedGenre = Genre.builder().id(4L).name("воздух").build();
    Genre actualGenre = genreDao.getById(4L);
    assertEquals(expectedGenre, actualGenre);
  }

  @Test
  void getById_idNotExists_success() {
    assertThrows(EntityNotFoundException.class, () -> genreDao.getById(6666L));
  }

  @Test
  void getIdByName_success() {
    long expectedId = 4L;
    long actualId = genreDao.getIdByName("воздух");
    assertEquals(expectedId, actualId);
  }

  @Test
  void update_success() {
    Genre newNameGenre = Genre.builder().id(4L).name("Новелла").build();
    genreDao.update(newNameGenre);
    Genre updatedGenre = genreDao.getById(4L);
    assertEquals(newNameGenre, updatedGenre);
  }

  @Test
  void deleteById_success() {
    String name = "some name";
    Genre newNameGenre = Genre.builder().name(name).build();
    genreDao.create(newNameGenre);
    Long id = genreDao.getIdByName(name);
    Genre createdGenre = genreDao.getById(id);
    assertEquals(newNameGenre.getName(), createdGenre.getName());
    genreDao.deleteById(id);
    assertThrows(EntityNotFoundException.class, () -> genreDao.getById(id));
  }
}