package com.study.spring.library.shell;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.study.spring.library.domain.Author;
import com.study.spring.library.exceptions.EntityNotFoundException;
import com.study.spring.library.io.LineWriter;
import com.study.spring.library.io.Printer;
import com.study.spring.library.io.UserInputReader;
import com.study.spring.library.service.AuthorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthorUserApiTest {

  @Spy
  private UserInputReader userInputReader;
  @Spy
  private LineWriter lineWriter;
  @Spy
  private AuthorService authorService;
  @Mock
  private Printer<Author> authorPrinter;
  @InjectMocks
  private AuthorUserApi authorUserApi;

  @Test
  void selectAndPerformOperation_createOption_success() {
    when(userInputReader.readIntFromLine()).thenReturn(1);
    authorUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(9)).writeLine(any());
    verify(authorService).create(any());
  }

  @Test
  void selectAndPerformOperation_deleteByIdOption_success() {
    when(userInputReader.readIntFromLine()).thenReturn(2);
    authorUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(9)).writeLine(any());
    verify(authorService).deleteById(any());
  }

  @Test
  void selectAndPerformOperation_getAllOption_success() {
    when(userInputReader.readIntFromLine()).thenReturn(3);
    authorUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(8)).writeLine(any());
    verify(authorService).getAll();
    verify(authorPrinter).print(anyList());
  }

  @Test
  void selectAndPerformOperation_getByIdOption_success() {
    String name = "testName";
    Author testAuthor = Author.builder().name(name).build();
    when(userInputReader.readIntFromLine()).thenReturn(4);
    when(authorService.findById(any())).thenReturn(testAuthor);
    authorUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(8)).writeLine(any());
    verify(authorService).findById(any());
    verify(authorPrinter).print(testAuthor);
  }

  @Test
  void selectAndPerformOperation_getByNameOption_success() {
    when(userInputReader.readIntFromLine()).thenReturn(5);
    authorUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(8)).writeLine(any());
    verify(authorService).findByName(any());
  }

  @Test
  void selectAndPerformOperation_updateOption_success() {
    when(userInputReader.readIntFromLine()).thenReturn(6);
    authorUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(10)).writeLine(any());
    verify(authorService).update(any());
  }

  @Test
  void selectAndPerformOperation_unknownOption_success() {
    when(userInputReader.readIntFromLine()).thenReturn(66);
    authorUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(8)).writeLine(any());
    verifyNoInteractions(authorService);
  }

  @Test
  void selectAndPerformOperation_authorNotFound_success() {
    when(userInputReader.readIntFromLine()).thenReturn(6);
    doThrow(EntityNotFoundException.class).when(authorService).update(any());
    authorUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(10)).writeLine(any());
    verify(authorService).update(any());
  }

  @Test
  void selectAndPerformOperation_sqlError_success() {
    when(userInputReader.readIntFromLine()).thenReturn(6);
    doThrow(new RuntimeException()).when(authorService).update(any());
    authorUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(10)).writeLine(any());
    verify(authorService).update(any());
  }
}
