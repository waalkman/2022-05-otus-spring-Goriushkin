package com.study.spring.library.shell;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.study.spring.library.dao.AuthorDao;
import com.study.spring.library.dao.BookDao;
import com.study.spring.library.dao.GenreDao;
import com.study.spring.library.domain.Author;
import com.study.spring.library.domain.Book;
import com.study.spring.library.domain.Genre;
import com.study.spring.library.exceptions.EntityNotFoundException;
import com.study.spring.library.io.LineWriter;
import com.study.spring.library.io.Printer;
import com.study.spring.library.io.UserInputReader;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

@ExtendWith(MockitoExtension.class)
class BookUserApiTest {

  @Spy
  private UserInputReader userInputReader;
  @Spy
  private LineWriter lineWriter;
  @Spy
  private BookDao bookDao;
  @Spy
  private GenreDao genreDao;
  @Spy
  private AuthorDao authorDao;
  @Mock
  private Printer<Book> bookPrinter;
  @InjectMocks
  private BookUserApi bookUserApi;

  @Test
  void selectAndPerformOperation_createOption_success() {
    when(userInputReader.readIntFromLine()).thenReturn(1);
    when(genreDao.findByName(any())).thenReturn(Optional.of(Genre.builder().name("testName").build()));
    when(authorDao.findByName(any())).thenReturn(Optional.of(Author.builder().name("testName").build()));
    bookUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(12)).writeLine(any());
    verify(bookDao).save(any());
  }

  @Test
  void selectAndPerformOperation_deleteOption_success() {
    when(userInputReader.readIntFromLine()).thenReturn(2);
    bookUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(9)).writeLine(any());
    verify(bookDao).deleteById(any());
  }

  @Test
  void selectAndPerformOperation_getAllOption_success() {
    when(userInputReader.readIntFromLine()).thenReturn(3);
    bookUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(8)).writeLine(any());
    verify(bookDao).findAll();
    verify(bookPrinter).print(anyList());
  }

  @Test
  void selectAndPerformOperation_getByIdOption_success() {
    String name = "testName";
    Optional<Book> testBook = Optional.of(Book.builder().title(name).build());
    when(userInputReader.readIntFromLine()).thenReturn(4);
    when(bookDao.findById(any())).thenReturn(testBook);
    bookUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(8)).writeLine(any());
    verify(bookDao).findById(any());
  }

  @Test
  void selectAndPerformOperation_updateOption_success() {
    when(userInputReader.readIntFromLine()).thenReturn(6);
    when(genreDao.findByName(any())).thenReturn(Optional.of(Genre.builder().name("testName").build()));
    when(authorDao.findByName(any())).thenReturn(Optional.of(Author.builder().name("testName").build()));
    bookUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(13)).writeLine(any());
    verify(bookDao).save(any());
  }

  @Test
  void selectAndPerformOperation_unknownOption_success() {
    when(userInputReader.readIntFromLine()).thenReturn(66);
    bookUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(8)).writeLine(any());
    verifyNoInteractions(bookDao);
  }

  @Test
  void selectAndPerformOperation_bookNotFound_success() {
    when(userInputReader.readIntFromLine()).thenReturn(6);
    when(genreDao.findByName(any())).thenReturn(Optional.of(Genre.builder().name("testName").build()));
    when(authorDao.findByName(any())).thenReturn(Optional.of(Author.builder().name("testName").build()));
    doThrow(EntityNotFoundException.class).when(bookDao).save(any());
    bookUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(13)).writeLine(any());
    verify(bookDao).save(any());
  }

  @Test
  void selectAndPerformOperation_sqlError_success() {
    when(userInputReader.readIntFromLine()).thenReturn(6);
    when(genreDao.findByName(any())).thenReturn(Optional.of(Genre.builder().name("testName").build()));
    when(authorDao.findByName(any())).thenReturn(Optional.of(Author.builder().name("testName").build()));
    doThrow(DataIntegrityViolationException.class).when(bookDao).save(any());
    bookUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(13)).writeLine(any());
    verify(bookDao).save(any());
  }
}
