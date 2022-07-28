package com.study.spring.library.shell;

import com.study.spring.library.dao.GenreDao;
import com.study.spring.library.domain.Genre;
import com.study.spring.library.exceptions.EntityNotFoundException;
import com.study.spring.library.exceptions.UnsupportedValueException;
import com.study.spring.library.io.LineWriter;
import com.study.spring.library.io.Printer;
import com.study.spring.library.io.UserInputReader;
import java.util.Optional;
import javax.persistence.PersistenceException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class GenreUserApi extends BaseUserApi {

  private static final String[] OPTIONS = {"create", "deleteById", "getAll", "getById", "getByName", "update"};

  private final GenreDao genreDao;
  private final Printer<Genre> printer;

  public GenreUserApi(
      UserInputReader userInputReader,
      LineWriter lineWriter,
      GenreDao genreDao,
      Printer<Genre> printer) {

    super(userInputReader, lineWriter);
    this.genreDao = genreDao;
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
  @Transactional
  protected void chooseOperation(String operation) {
    switch (operation) {
      case "create" -> create();
      case "deleteById" -> deleteById();
      case "getAll" -> getAll();
      case "getById" -> getByid();
      case "getByName" -> getIdByName();
      case "update" -> update();
      default -> throw new UnsupportedValueException(String.format("Unsupported option requested: %s", operation));
    }
  }

  private void update() {
    getLineWriter().writeLine("Enter genre id:");
    long id = getUserInputReader().readLongFromLine();
    getLineWriter().writeLine("Enter genre name:");
    String name = getUserInputReader().readLine();
    genreDao.update(new Genre(id, name));
    getLineWriter().writeLine("Genre updated");
  }

  private void create() {
    getLineWriter().writeLine("Enter new genre:");
    String name = getUserInputReader().readLine();
    genreDao.create(Genre.builder().name(name).build());
    getLineWriter().writeLine("Genre created");
  }

  private void getAll() {
    getLineWriter().writeLine("All genres:");
    printer.print(genreDao.getAll());
  }

  private void getByid() {
    getLineWriter().writeLine("Enter genre id:");
    long id = getUserInputReader().readLongFromLine();
    Genre genre = genreDao.getById(id);
    getLineWriter().writeLine("Result:");
    printer.print(Optional.of(genre));
  }

  private void getIdByName() {
    getLineWriter().writeLine("Enter genre name:");
    String name = getUserInputReader().readLine();
    Genre genre = genreDao.getByName(name);
    printer.print(Optional.of(genre));
  }

  private void deleteById() {
    getLineWriter().writeLine("Enter genre id:");
    long id = getUserInputReader().readLongFromLine();
    genreDao.deleteById(id);
    getLineWriter().writeLine("Deleted successfully");
  }

}
