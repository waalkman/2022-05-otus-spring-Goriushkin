package com.study.spring.library.shell;

import com.study.spring.library.domain.Genre;
import com.study.spring.library.exceptions.EntityNotFoundException;
import com.study.spring.library.exceptions.UnsupportedValueException;
import com.study.spring.library.io.LineWriter;
import com.study.spring.library.io.Printer;
import com.study.spring.library.io.UserInputReader;
import com.study.spring.library.service.GenreService;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.persistence.PersistenceException;
import org.springframework.stereotype.Component;

@Component
public class GenreUserApi extends BaseUserApi {

  private static final String[] OPTIONS = new String[GenreService.class.getDeclaredMethods().length];
  static {
    Arrays.stream(GenreService.class.getDeclaredMethods())
          .map(Method::getName)
          .sorted()
          .collect(Collectors.toList())
          .toArray(OPTIONS);
  }

  private final GenreService genreService;
  private final Printer<Genre> printer;

  public GenreUserApi(
      UserInputReader userInputReader,
      LineWriter lineWriter,
      GenreService genreService,
      Printer<Genre> printer) {

    super(userInputReader, lineWriter);
    this.genreService = genreService;
    this.printer = printer;
  }

  @Override
  protected String[] getOptions() {
    return OPTIONS;
  }

  @Override
  protected void runOperation(String operation) {
    try {
      chooseOperation(operation);
    } catch (EntityNotFoundException ex) {
      getLineWriter().writeLine("Genre(s) not found");
    } catch (PersistenceException e) {
      getLineWriter().writeLine(String.format("Error executing operation %s", e.getMessage()));
    }
  }

  @Override
  protected void chooseOperation(String operation) {
    switch (operation) {
      case "update":
        update();
        break;
      case "create":
        create();
        break;
      case "getAll":
        getAll();
        break;
      case "getById":
        getByid();
        break;
      case "getByName":
        getIdByName();
        break;
      case "deleteById":
        deleteById();
        break;
      default:
        throw new UnsupportedValueException(String.format("Unsupported option requested: %s", operation));
    }
  }

  private void update() {
    getLineWriter().writeLine("Enter genre id:");
    long id = getUserInputReader().readLongFromLine();
    getLineWriter().writeLine("Enter genre name:");
    String name = getUserInputReader().readLine();
    genreService.update(new Genre(id, name));
    getLineWriter().writeLine("Genre updated");
  }

  private void create() {
    getLineWriter().writeLine("Enter new genre:");
    String name = getUserInputReader().readLine();
    genreService.create(Genre.builder().name(name).build());
    getLineWriter().writeLine("Genre created");
  }

  private void getAll() {
    getLineWriter().writeLine("All genres:");
    printer.print(genreService.getAll());
  }

  private void getByid() {
    getLineWriter().writeLine("Enter genre id:");
    long id = getUserInputReader().readLongFromLine();
    Genre genre = genreService.getById(id);
    getLineWriter().writeLine("Result:");
    printer.print(genre);
  }

  private void getIdByName() {
    getLineWriter().writeLine("Enter genre name:");
    String name = getUserInputReader().readLine();
    Genre genre = genreService.getByName(name);
    printer.print(genre);
  }

  private void deleteById() {
    getLineWriter().writeLine("Enter genre id:");
    long id = getUserInputReader().readLongFromLine();
    genreService.deleteById(id);
    getLineWriter().writeLine("Deleted successfully");
  }

}
