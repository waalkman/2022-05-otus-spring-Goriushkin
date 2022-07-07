package com.study.spring.library.shell;

import com.study.spring.library.dao.GenreDao;
import com.study.spring.library.domain.Genre;
import com.study.spring.library.exceptions.UnsupportedValueException;
import com.study.spring.library.io.LineWriter;
import com.study.spring.library.io.Printer;
import com.study.spring.library.io.UserInputReader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class GenreUserApi extends BaseUserApi {

  private static final Map<Integer, String> OPTIONS = new HashMap<>();
  static {
    Method[] methods = GenreDao.class.getDeclaredMethods();
    for (int i = 0; i < methods.length; ++i) {
      OPTIONS.put(i + 1, methods[i].getName());
    }
  }

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
  protected Map<Integer, String> getOptions() {
    return OPTIONS;
  }

  @Override
  protected void runOperation(String operation) {
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
      case "getIdByName":
        getIdByName();
        break;
      case "deleteById":
        deleteById();
        break;
      default:
        throw new UnsupportedValueException(String.format("Unsupported option requested: %s", operation));
    }
  }

  public void update() {
    lineWriter.writeLine("Enter genre id:");
    long id = userInputReader.readLongFromLine();
    lineWriter.writeLine("Enter genre name:");
    String name = userInputReader.readLine();
    genreDao.update(new Genre(id, name));
    lineWriter.writeLine("Genre updated");
  }

  public void create() {
    lineWriter.writeLine("Enter new genre:");
    String name = userInputReader.readLine();
    genreDao.create(Genre.builder().name(name).build());
    lineWriter.writeLine("Genre created");
  }

  public void getAll() {
    lineWriter.writeLine("All genres:");
    printer.print(genreDao.getAll());
  }

  private void getByid() {
    lineWriter.writeLine("Enter genre id:");
    long id = userInputReader.readLongFromLine();
    Genre genre = genreDao.getById(id);
    lineWriter.writeLine("Result:");
    printer.print(genre);
  }

  private void getIdByName() {
    lineWriter.writeLine("Enter genre name:");
    String name = userInputReader.readLine();
    Long id = genreDao.getIdByName(name);
    lineWriter.writeLine("Result:");
    lineWriter.writeLine(id + "");
  }

  private void deleteById() {
    lineWriter.writeLine("Enter genre id:");
    long id = userInputReader.readLongFromLine();
    genreDao.deleteById(id);
    lineWriter.writeLine("Deleted successfully");
  }

}
