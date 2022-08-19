package com.study.spring.library.shell;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.study.spring.library.domain.Comment;
import com.study.spring.library.exceptions.EntityNotFoundException;
import com.study.spring.library.io.LineWriter;
import com.study.spring.library.io.Printer;
import com.study.spring.library.io.UserInputReader;
import com.study.spring.library.service.CommentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentUserApiTest {

  @Spy
  private UserInputReader userInputReader;
  @Spy
  private LineWriter lineWriter;
  @Spy
  private CommentService commentService;
  @Mock
  private Printer<Comment> commentPrinter;
  @InjectMocks
  private CommentUserApi commentUserApi;

  @Test
  void selectAndPerformOperation_createOption_success() {
    when(userInputReader.readIntFromLine()).thenReturn(1);
    commentUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(10)).writeLine(any());
    verify(commentService).create(any(), any());
  }

  @Test
  void selectAndPerformOperation_deleteOption_success() {
    when(userInputReader.readIntFromLine()).thenReturn(2);
    commentUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(9)).writeLine(any());
    verify(commentService).deleteById(any(), any());
  }

  @Test
  void selectAndPerformOperation_findByBookTitleOption_success() {
    when(userInputReader.readIntFromLine()).thenReturn(3);
    commentUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(7)).writeLine(any());
    verify(commentService).findByBookTitle(any());
    verify(commentPrinter).print(anyList());
  }

  @Test
  void selectAndPerformOperation_getByIdOption_success() {
    String name = "testName";
    Comment testComment = Comment.builder().userName(name).text("text").build();
    when(userInputReader.readIntFromLine()).thenReturn(4);
    when(commentService.findById(any(), any())).thenReturn(testComment);
    commentUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(8)).writeLine(any());
    verify(commentService).findById(any(), any());
  }

  @Test
  void selectAndPerformOperation_updateOption_success() {
    when(userInputReader.readIntFromLine()).thenReturn(5);
    commentUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(11)).writeLine(any());
    verify(commentService).update(any(), any());
  }

  @Test
  void selectAndPerformOperation_unknownOption_success() {
    when(userInputReader.readIntFromLine()).thenReturn(66);
    commentUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(7)).writeLine(any());
    verifyNoInteractions(commentService);
  }

  @Test
  void selectAndPerformOperation_commentNotFound_success() {
    when(userInputReader.readIntFromLine()).thenReturn(5);
    doThrow(EntityNotFoundException.class).when(commentService).update(any(), any());
    commentUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(11)).writeLine(any());
    verify(commentService).update(any(), any());
  }

  @Test
  void selectAndPerformOperation_sqlError_success() {
    when(userInputReader.readIntFromLine()).thenReturn(5);
    doThrow(new RuntimeException()).when(commentService).update(any(), any());
    commentUserApi.selectAndPerformOperation();
    verify(userInputReader).readIntFromLine();
    verify(lineWriter, times(11)).writeLine(any());
    verify(commentService).update(any(), any());
  }
}
