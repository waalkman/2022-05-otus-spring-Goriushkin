package com.study.spring.library.shell;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.study.spring.library.domain.Book;
import com.study.spring.library.dto.CommentedBook;
import com.study.spring.library.exceptions.EntityNotFoundException;
import com.study.spring.library.io.LineWriter;
import com.study.spring.library.io.Printer;
import com.study.spring.library.io.UserInputReader;
import com.study.spring.library.service.BookService;
import javax.persistence.PersistenceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookUserApiTest {

  @Spy
  private UserInputReader userInputReader;
  @Spy
  private LineWriter lineWriter;
  @Spy
  private BookService bookService;
  @Mock
  private Printer<Book> bookPrinter;
  @InjectMocks
  private BookUserApi bookUserApi;

  @Test
  void selectAndPerformOperation_createOption_success() {
    when(userInputReader.readIntFromLine()).thenReturn(1);
    bookUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(12)).writeLine(any());
    verify(bookService).create(any(), any(), any());
  }

  @Test
  void selectAndPerformOperation_deleteOption_success() {
    when(userInputReader.readIntFromLine()).thenReturn(2);
    bookUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(9)).writeLine(any());
    verify(bookService).deleteById(any());
  }

  @Test
  void selectAndPerformOperation_getAllOption_success() {
    when(userInputReader.readIntFromLine()).thenReturn(3);
    bookUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(8)).writeLine(any());
    verify(bookService).getAll();
    verify(bookPrinter).print(anyList());
  }

  @Test
  void selectAndPerformOperation_getByIdOption_success() {
    String name = "testName";
    Book testBook = Book.builder().title(name).build();
    when(userInputReader.readIntFromLine()).thenReturn(4);
    when(bookService.findById(any())).thenReturn(new CommentedBook(testBook));
    bookUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(9)).writeLine(any());
    verify(bookService).findById(any());
  }

  @Test
  void selectAndPerformOperation_updateOption_success() {
    when(userInputReader.readIntFromLine()).thenReturn(6);
    bookUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(13)).writeLine(any());
    verify(bookService).update(any(), any(), any());
  }

  @Test
  void selectAndPerformOperation_unknownOption_success() {
    when(userInputReader.readIntFromLine()).thenReturn(66);
    bookUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(8)).writeLine(any());
    verifyNoInteractions(bookService);
  }

  @Test
  void selectAndPerformOperation_bookNotFound_success() {
    when(userInputReader.readIntFromLine()).thenReturn(6);
    doThrow(EntityNotFoundException.class).when(bookService).update(any(), any(), any());
    bookUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(13)).writeLine(any());
    verify(bookService).update(any(), any(), any());
  }

  @Test
  void selectAndPerformOperation_sqlError_success() {
    when(userInputReader.readIntFromLine()).thenReturn(6);
    doThrow(PersistenceException.class).when(bookService).update(any(), any(), any());
    bookUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(13)).writeLine(any());
    verify(bookService).update(any(), any(), any());
  }
}
