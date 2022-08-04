package com.study.spring.library.shell;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.study.spring.library.domain.Genre;
import com.study.spring.library.exceptions.EntityNotFoundException;
import com.study.spring.library.io.LineWriter;
import com.study.spring.library.io.Printer;
import com.study.spring.library.io.UserInputReader;
import com.study.spring.library.service.GenreService;
import javax.persistence.PersistenceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GenreUserApiTest {

  @Spy
  private UserInputReader userInputReader;
  @Spy
  private LineWriter lineWriter;
  @Spy
  private GenreService genreService;
  @Mock
  private Printer<Genre> genrePrinter;
  @InjectMocks
  private GenreUserApi genreUserApi;

  @Test
  void selectAndPerformOperation_createOption_success() {
    when(userInputReader.readIntFromLine()).thenReturn(1);
    genreUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(9)).writeLine(any());
    verify(genreService).create(any());
  }

  @Test
  void selectAndPerformOperation_deleteByIdOption_success() {
    when(userInputReader.readIntFromLine()).thenReturn(2);
    genreUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(9)).writeLine(any());
    verify(genreService).deleteById(any());
  }

  @Test
  void selectAndPerformOperation_getAllOption_success() {
    when(userInputReader.readIntFromLine()).thenReturn(3);
    genreUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(8)).writeLine(any());
    verify(genreService).getAll();
    verify(genrePrinter).print(anyList());
  }

  @Test
  void selectAndPerformOperation_getByIdOption_success() {
    String name = "testName";
    Genre testGenre = Genre.builder().name(name).build();
    when(userInputReader.readIntFromLine()).thenReturn(4);
    when(genreService.getById(any())).thenReturn(testGenre);
    genreUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(9)).writeLine(any());
    verify(genreService).getById(any());
    verify(genrePrinter).print(testGenre);
  }

  @Test
  void selectAndPerformOperation_getIdByNameOption_success() {
    when(userInputReader.readIntFromLine()).thenReturn(5);
    genreUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(8)).writeLine(any());
    verify(genreService).getByName(any());
  }

  @Test
  void selectAndPerformOperation_updateOption_success() {
    when(userInputReader.readIntFromLine()).thenReturn(6);
    genreUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(10)).writeLine(any());
    verify(genreService).update(any());
  }

  @Test
  void selectAndPerformOperation_unknownOption_success() {
    when(userInputReader.readIntFromLine()).thenReturn(66);
    genreUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(8)).writeLine(any());
    verifyNoInteractions(genreService);
  }

  @Test
  void selectAndPerformOperation_authorNotFound_success() {
    when(userInputReader.readIntFromLine()).thenReturn(6);
    doThrow(EntityNotFoundException.class).when(genreService).update(any());
    genreUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(10)).writeLine(any());
    verify(genreService).update(any());
  }

  @Test
  void selectAndPerformOperation_sqlError_success() {
    when(userInputReader.readIntFromLine()).thenReturn(6);
    doThrow(PersistenceException.class).when(genreService).update(any());
    genreUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(10)).writeLine(any());
    verify(genreService).update(any());
  }

}
