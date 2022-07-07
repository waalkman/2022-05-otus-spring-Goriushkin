package com.study.spring.library.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@RequiredArgsConstructor
public class ShellApi {

  private final GenreUserApi genreUserApi;
  private final AuthorUserApi authorUserApi;
  private final BookUserApi bookUserApi;

  @ShellMethod(key = {"genre", "g"}, value = "Genre api")
  public void genreApi() {
    int option = genreUserApi.chooseOptionMenu();
    genreUserApi.acceptOption(option);
  }

  @ShellMethod(key = {"author", "a"}, value = "Author api")
  public void authorApi() {
    int option = authorUserApi.chooseOptionMenu();
    authorUserApi.acceptOption(option);
  }

  @ShellMethod(key = {"book", "b"}, value = "Book api")
  public void bookApi() {
    int option = bookUserApi.chooseOptionMenu();
    bookUserApi.acceptOption(option);
  }
}