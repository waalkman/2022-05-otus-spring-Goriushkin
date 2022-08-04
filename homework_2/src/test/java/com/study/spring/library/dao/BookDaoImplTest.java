package com.study.spring.library.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.study.spring.library.domain.Book;
import com.study.spring.library.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({BookDaoImpl.class, AuthorDaoImpl.class, GenreDaoImpl.class})
class BookDaoImplTest {

  @Autowired
  private BookDao bookDao;
  @Autowired
  private TestEntityManager em;

  @Test
  void getAll_success() {
    int expectedAuthorsAmount = 2;
    int actualAuthorsAmount = bookDao.getAll()
                                     .size();
    assertEquals(expectedAuthorsAmount, actualAuthorsAmount);
  }

  @Test
  void create_success() {
    Book book = DaoTestUtils.createBook(em, "Test title", "Some description");
    Long bookId = book.getId();

    Book actualBook = em.find(Book.class, bookId);

    assertEquals(book, actualBook);
  }

  @Test
  void getById_success() {
    Book book = createBookWithDefaults();
    em.detach(book);
    Long bookId = book.getId();

    Book bookById = bookDao.getById(bookId);

    assertEquals(book, bookById);
  }

  @Test
  void getById_notExistingBook_success() {
    assertThrows(EntityNotFoundException.class, () -> bookDao.getById(Long.MAX_VALUE));
  }

  @Test
  void update_success() {
    String newTitle = "updated title";
    String newDescription = "updated description";
    Book newBookData = createBookWithDefaults();
    em.detach(newBookData);

    newBookData.setTitle(newTitle);
    newBookData.setDescription(newDescription);

    bookDao.save(newBookData);

    Book bookFromDb = em.find(Book.class, newBookData.getId());

    assertEquals(newTitle, bookFromDb.getTitle());
    assertEquals(newDescription, bookFromDb.getDescription());
  }

  @Test
  void update_notExistingBook_success() {
    Book newBookData = createBookWithDefaults();
    em.detach(newBookData);

    newBookData.setId(Long.MAX_VALUE);

    assertThrows(EntityNotFoundException.class, () -> bookDao.save(newBookData));
  }

  @Test
  void deleteById_success() {
    Book newBookData = createBookWithDefaults();
    Long id = newBookData.getId();
    em.detach(newBookData);

    bookDao.deleteById(id);

    assertNull(em.find(Book.class, id));
  }

  private Book createBookWithDefaults() {
    String expectedTitle = "title_exp";
    String expectedDescription = "title_descr";
    return DaoTestUtils.createBook(em, expectedTitle, expectedDescription);
  }

}