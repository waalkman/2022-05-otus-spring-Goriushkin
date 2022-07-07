package com.study.spring.library.shell;

import com.study.spring.library.dao.AuthorDao;
import com.study.spring.library.domain.Author;
import com.study.spring.library.exceptions.UnsupportedValueException;
import com.study.spring.library.io.AuthorPrinter;
import com.study.spring.library.io.LineWriter;
import com.study.spring.library.io.UserInputReader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class AuthorUserApi extends BaseUserApi {

  private static final Map<Integer, String> OPTIONS = new HashMap<>();
  static {
    Method[] methods = AuthorDao.class.getDeclaredMethods();
    for (int i = 0; i < methods.length; ++i) {
      OPTIONS.put(i + 1, methods[i].getName());
    }
  }

  private final AuthorDao authorDao;
  private final AuthorPrinter authorPrinter;

  public AuthorUserApi(
      UserInputReader userInputReader,
      LineWriter lineWriter,
      AuthorDao authorDao,
      AuthorPrinter authorPrinter) {

    super(userInputReader, lineWriter);
    this.authorDao = authorDao;
    this.authorPrinter = authorPrinter;
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
    lineWriter.writeLine("Enter author id:");
    long id = userInputReader.readLongFromLine();
    lineWriter.writeLine("Enter author name:");
    String name = userInputReader.readLine();
    authorDao.update(new Author(id, name));
    lineWriter.writeLine("Author updated");
  }

  public void create() {
    lineWriter.writeLine("Enter new author name:");
    String name = userInputReader.readLine();
    authorDao.create(Author.builder().name(name).build());
    lineWriter.writeLine("Author created");
  }

  public void getAll() {
    lineWriter.writeLine("All authors:");
    authorPrinter.print(authorDao.getAll());
  }

  private void getIdByName() {
    lineWriter.writeLine("Enter author name:");
    String name = userInputReader.readLine();
    Long id = authorDao.getIdByName(name);
    lineWriter.writeLine("Result:");
    lineWriter.writeLine(id + "");
  }

  private void getByid() {
    lineWriter.writeLine("Enter author id:");
    long id = userInputReader.readLongFromLine();
    Author author = authorDao.getById(id);
    lineWriter.writeLine("Result:");
    authorPrinter.print(author);
  }

  private void deleteById() {
    lineWriter.writeLine("Enter author id:");
    long id = userInputReader.readLongFromLine();
    authorDao.deleteById(id);
    lineWriter.writeLine("Deleted successfully");
  }

}
