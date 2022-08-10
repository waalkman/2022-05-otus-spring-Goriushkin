package com.study.spring.library.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.study.spring.library.domain.Author;
import com.study.spring.library.exceptions.EntityNotFoundException;
import javax.persistence.PersistenceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(AuthorDaoImpl.class)
class AuthorDaoImplTest {

  @Autowired
  private AuthorDao authorDao;
  @Autowired
  private TestEntityManager em;

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
    long id = authorDao.save(author);
    Author createdAuthor = em.find(Author.class, id);
    assertEquals(author, createdAuthor);
  }

  @Test
  void create_tooLongName_success() {
    String name = "some name that is veeeeeery looooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong";
    Author author = Author.builder().name(name).build();
    assertThrows(PersistenceException.class, () -> authorDao.save(author));
  }

  @Test
  void getById_success() {
    Author expectedAuthor = DaoTestUtils.createAuthor(em);
    Author actualAuthor = authorDao.getById(expectedAuthor.getId());
    assertEquals(expectedAuthor, actualAuthor);
  }

  @Test
  void getById_idNotExists_success() {
    assertThrows(EntityNotFoundException.class, () -> authorDao.getById(6666L));
  }

  @Test
  void getIdByName_success() {
    Author expectedAuthor = DaoTestUtils.createAuthor(em);
    String authorName = expectedAuthor.getName();
    Author author = authorDao.getByName(authorName);
    assertEquals(expectedAuthor, author);
  }

  @Test
  void update_success() {
    String updatedName = "Updated name";
    Author expectedAuthor = DaoTestUtils.createAuthor(em);
    em.detach(expectedAuthor);
    expectedAuthor.setName(updatedName);

    authorDao.save(expectedAuthor);

    Author actualAuthor = em.find(Author.class, expectedAuthor.getId());

    assertEquals(expectedAuthor, actualAuthor);
  }

  @Test
  void update_notExistingAuthor_success() {
    Author author = DaoTestUtils.createAuthor(em);
    em.detach(author);

    author.setId(Long.MAX_VALUE);

    assertThrows(EntityNotFoundException.class, () -> authorDao.save(author));
  }

  @Test
  void deleteById_success() {
    Author createdAuthor = DaoTestUtils.createAuthor(em);
    Long id = createdAuthor.getId();
    em.detach(createdAuthor);

    authorDao.deleteById(id);

    assertNull(em.find(Author.class, id));
  }

}