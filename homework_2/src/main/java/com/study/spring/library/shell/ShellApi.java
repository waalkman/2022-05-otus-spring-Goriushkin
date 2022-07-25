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
}