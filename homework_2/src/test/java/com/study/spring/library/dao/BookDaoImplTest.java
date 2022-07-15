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
    Book book = Book.builder()
                    .title("title")
                    .description("decsr")
                    .authorId(1L)
                    .genreId(1L)
                    .build();

    long bookId = bookDao.create(book);
    Book bookById = bookDao.getById(bookId);
    assertEquals(book.getTitle(), bookById.getTitle());
    assertEquals(book.getDescription(), bookById.getDescription());
    assertEquals(book.getAuthorId(), bookById.getAuthorId());
    assertEquals(book.getGenreId(), bookById.getGenreId());
  }

  @Test
  void getById_success() {
    Book book = bookDao.getById(1L);

    assertEquals("Как понять слона", book.getTitle());
    assertEquals("Слоны для чайников", book.getDescription());
    assertEquals(2, book.getAuthorId());
    assertEquals(4, book.getGenreId());
  }

  @Test
  void update_success() {
    Book newBookData = Book.builder()
                           .id(1L)
                           .title("title")
                           .description("decsr")
                           .authorId(1L)
                           .genreId(1L)
                           .build();

    bookDao.update(newBookData);
    Book bookFromDb = bookDao.getById(1L);

    assertEquals(newBookData, bookFromDb);
  }

  @Test
  void deleteById_success() {
    bookDao.deleteById(1L);
    assertThrows(EntityNotFoundException.class, () -> bookDao.getById(1L));
  }
}