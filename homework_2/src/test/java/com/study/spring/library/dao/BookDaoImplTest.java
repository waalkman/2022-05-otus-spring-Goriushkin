package com.study.spring.library.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.study.spring.library.domain.Book;
import com.study.spring.library.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

@JdbcTest
@Import(BookDaoImpl.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookDaoImplTest {

  @Autowired
  private BookDaoImpl bookDao;

  @Test
  void getAll_success() {
    int expectedAuthorsAmount = 2;
    int actualAuthorsAmount = bookDao.getAll().size();
    assertEquals(expectedAuthorsAmount, actualAuthorsAmount);
  }

  @Test
  void create_success() {
    Book book = getFirstBook();
    long bookId = bookDao.create(book, 4L, 2L);
    Book bookById = bookDao.getById(bookId);
    assertEquals(book.getTitle(), bookById.getTitle());
    assertEquals(book.getDescription(), bookById.getDescription());
    assertEquals("Ваня", bookById.getAuthor());
    assertEquals("воздух", bookById.getGenre());
  }

  @Test
  void getById_success() {
    Book book = bookDao.getById(1L);

    assertEquals("Как понять слона", book.getTitle());
    assertEquals("Слоны для чайников", book.getDescription());
    assertEquals("Ваня", book.getAuthor());
    assertEquals("воздух", book.getGenre());
  }

  @Test
  void update_success() {
    Book newBookData = Book.builder()
                           .id(1L)
                           .title("title_upd")
                           .description("decsr_upd")
                           .author("Маня")
                           .genre("конь")
                           .build();

    bookDao.update(newBookData, 1L, 3L);
    Book bookFromDb = bookDao.getById(1L);

    assertEquals(newBookData, bookFromDb);
  }

  @Test
  void deleteById_success() {
    bookDao.deleteById(1L);
    assertThrows(EntityNotFoundException.class, () -> bookDao.getById(1L));
  }

  private static Book getFirstBook() {
    return Book.builder()
               .id(1L)
               .title("title")
               .description("decsr")
               .author("Ваня")
               .genre("воздух")
               .build();
  }
}