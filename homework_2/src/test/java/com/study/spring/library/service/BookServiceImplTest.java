package com.study.spring.library.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.study.spring.library.dao.AuthorDao;
import com.study.spring.library.dao.BookDao;
import com.study.spring.library.dao.GenreDao;
import com.study.spring.library.domain.Author;
import com.study.spring.library.domain.Book;
import com.study.spring.library.domain.Genre;
import com.study.spring.library.exceptions.EntityNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

  @Mock
  private BookDao bookDao;
  @Mock
  private AuthorDao authorDao;
  @Mock
  private GenreDao genreDao;

  @InjectMocks
  private BookServiceImpl bookService;

  @Test
  void getAll_success() {
    bookService.findAll();
    verify(bookDao).findAll();
  }

  @Test
  void create_success() {
    String genreName = "test_genre";
    String authorName = "test_author";
    Genre genre = Genre.builder().name(genreName).build();
    Author author = Author.builder().name(authorName).build();
    Book book = Book.builder()
                    .title("t")
                    .description("d")
                    .genre(genre)
                    .author(author)
                    .build();

    when(authorDao.findByName(authorName)).thenReturn(Mono.just(author));
    when(genreDao.findByName(genreName)).thenReturn(Mono.just(genre));

    bookService.create(book, genreName, authorName);

    verify(genreDao).findByName(genreName);
    verify(authorDao).findByName(authorName);
    verify(bookDao).save(book);
  }

  //TODO
  //TODO
  //TODO
  //TODO
  //TODO


//
//  @Test
//  void findById_success() {
//    String bookId = "123";
//    Book expectedBook = Book.builder().id(bookId).title("test").build();
//    when(bookDao.findById(bookId)).thenReturn(Optional.of(expectedBook));
//
//    Book actualBook = bookService.findById(bookId);
//
//    assertEquals(expectedBook, actualBook);
//  }
//
//  @Test
//  void findById_notFound() {
//    when(bookDao.findById(any())).thenReturn(Optional.empty());
//
//    assertThrows(EntityNotFoundException.class, () -> bookService.findById(any()));
//  }
//
//  @Test
//  void findByTitle_success() {
//    String title = "title_";
//    Book expectedBook = Book.builder().id("123").title(title).build();
//    when(bookDao.findByTitle(title)).thenReturn(Optional.of(expectedBook));
//
//    Book actualBook = bookService.findByTitle(title);
//
//    assertEquals(expectedBook, actualBook);
//  }
//
//  @Test
//  void findByTitle_notFound() {
//    when(bookDao.findByTitle(any())).thenReturn(Optional.empty());
//
//    assertThrows(EntityNotFoundException.class, () -> bookService.findByTitle(any()));
//  }
//
//  @Test
//  void update_success() {
//    String bookId = "idd";
//    String genreName = "test_genre";
//    String authorName = "test_author";
//    Genre genre = Genre.builder().name(genreName).build();
//    Author author = Author.builder().name(authorName).build();
//    Book book = Book.builder()
//                    .id(bookId)
//                    .title("t")
//                    .description("d")
//                    .genre(genre)
//                    .author(author)
//                    .build();
//
//    when(bookDao.findById(bookId)).thenReturn(Optional.of(book));
//    when(authorDao.findByName(authorName)).thenReturn(Optional.of(author));
//    when(genreDao.findByName(genreName)).thenReturn(Optional.of(genre));
//
//    bookService.update(book, genreName, authorName);
//
//    verify(bookDao).findById(bookId);
//    verify(bookDao).save(book);
//  }
//
//  @Test
//  void update_notFound() {
//    Book book = Book.builder()
//                    .id("123")
//                    .title("t")
//                    .build();
//
//    when(bookDao.findById(any())).thenReturn(Optional.empty());
//
//    assertThrows(EntityNotFoundException.class, () -> bookService.update(book, null, null));
//  }
//
//  @Test
//  void deleteById_success() {
//    String bookId = "123";
//
//    bookService.deleteById(bookId);
//
//    verify(bookDao).deleteById(bookId);
//  }

}