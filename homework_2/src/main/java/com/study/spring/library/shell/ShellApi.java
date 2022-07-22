package com.study.spring.library.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@RequiredArgsConstructor
public class ShellApi {

  @Qualifier("genreUserApi")
  private final BaseUserApi genreUserApi;
  @Qualifier("authorUserApi")
  private final BaseUserApi authorUserApi;
  @Qualifier("bookUserApi")
  private final BaseUserApi bookUserApi;
  @Qualifier("commentUserApi")
  private final CommentUserApi commentUserApi;

  @ShellMethod(key = {"genre", "g"}, value = "Genre api")
  public void genreApi() {
    genreUserApi.selectAndPerformOperation();
  }

  @ShellMethod(key = {"author", "a"}, value = "Author api")
  public void authorApi() {
    authorUserApi.selectAndPerformOperation();
  }

  @ShellMethod(key = {"book", "b"}, value = "Book api")
  public void bookApi() {
    bookUserApi.selectAndPerformOperation();
  }

  @ShellMethod(key = {"comment", "c"}, value = "Comments api")
  public void commentsApi() {
    commentUserApi.selectAndPerformOperation();
  }
}