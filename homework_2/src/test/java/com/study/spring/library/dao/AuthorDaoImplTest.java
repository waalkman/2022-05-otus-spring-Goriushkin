package com.study.spring.library.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.study.spring.library.domain.Author;
import com.study.spring.library.exceptions.DataQueryException;
import com.study.spring.library.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

@JdbcTest
@Import(AuthorDaoImpl.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthorDaoImplTest {

  @Autowired
  private AuthorDao authorDao;

  @Test
  void getAll() {
    int expectedAuthorsAmount = 5;
    int actualAuthorsAmount = authorDao.getAll().size();
    assertEquals(expectedAuthorsAmount, actualAuthorsAmount);
  }

  @Test
  void create_success() {
    String name = "some name";
    Author author = Author.builder().name(name).build();
    long id = authorDao.create(author);
    Author createdAuthor = authorDao.getById(id);
    assertEquals(author.getName(), createdAuthor.getName());
  }

  @Test
  void create_tooLongName_success() {
    String name = "some name that is veeeeeery looooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong";
    Author author = Author.builder().name(name).build();
    assertThrows(DataQueryException.class, () -> authorDao.create(author));
  }

  @Test
  void getById_success() {
    Author expectedAuthor = Author.builder().id(4L).name("Таня").build();
    Author actualAuthor = authorDao.getById(4L);
    assertEquals(expectedAuthor, actualAuthor);
  }

  @Test
  void getById_idNotExists_success() {
    assertThrows(EntityNotFoundException.class, () -> authorDao.getById(6666L));
  }

  @Test
  void getIdByName_success() {
    long expectedId = 4L;
    long actualId = authorDao.getIdByName("Таня");
    assertEquals(expectedId, actualId);
  }

  @Test
  void update_success() {
    Author newNameAuthor = Author.builder().id(4L).name("Кирилл").build();
    authorDao.update(newNameAuthor);
    Author updatedAuthor = authorDao.getById(4L);
    assertEquals(newNameAuthor, updatedAuthor);
  }

  @Test
  void deleteById_success() {
    String name = "some name";
    Author newNameAuthor = Author.builder().name(name).build();
    authorDao.create(newNameAuthor);
    Long id = authorDao.getIdByName(name);
    Author createdAuthor = authorDao.getById(id);
    assertEquals(newNameAuthor.getName(), createdAuthor.getName());
    authorDao.deleteById(id);
    assertThrows(EntityNotFoundException.class, () -> authorDao.getById(id));
  }
}