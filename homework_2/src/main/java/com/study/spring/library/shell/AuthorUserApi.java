package com.study.spring.library.shell;

import com.study.spring.library.dao.AuthorDao;
import com.study.spring.library.domain.Author;
import com.study.spring.library.exceptions.DataQueryException;
import com.study.spring.library.exceptions.EntityNotFoundException;
import com.study.spring.library.exceptions.UnsupportedValueException;
import com.study.spring.library.io.LineWriter;
import com.study.spring.library.io.Printer;
import com.study.spring.library.io.UserInputReader;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class AuthorUserApi extends BaseUserApi {

  private static final String[] OPTIONS = new String[AuthorDao.class.getDeclaredMethods().length];
  static {
    Arrays.stream(AuthorDao.class.getDeclaredMethods())
          .map(Method::getName)
          .sorted()
          .collect(Collectors.toList())
          .toArray(OPTIONS);
  }

  private final AuthorDao authorDao;
  private final Printer<Author> authorPrinter;

  public AuthorUserApi(
      UserInputReader userInputReader,
      LineWriter lineWriter,
      AuthorDao authorDao,
      Printer<Author> authorPrinter) {

    super(userInputReader, lineWriter);
    this.authorDao = authorDao;
    this.authorPrinter = authorPrinter;
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
      getLineWriter().writeLine("Author(s) not found");
    } catch (DataQueryException e) {
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

  private void update() {
    getLineWriter().writeLine("Enter author id:");
    long id = getUserInputReader().readLongFromLine();
    getLineWriter().writeLine("Enter author name:");
    String name = getUserInputReader().readLine();
    authorDao.update(new Author(id, name));
    getLineWriter().writeLine("Author updated");
  }

  private void create() {
    getLineWriter().writeLine("Enter new author name:");
    String name = getUserInputReader().readLine();
    authorDao.create(Author.builder().name(name).build());
    getLineWriter().writeLine("Author created");
  }

  private void getAll() {
    getLineWriter().writeLine("All authors:");
    authorPrinter.print(authorDao.getAll());
  }

  private void getIdByName() {
    getLineWriter().writeLine("Enter author name:");
    String name = getUserInputReader().readLine();
    Long id = authorDao.getIdByName(name);
    getLineWriter().writeLine("Result:");
    getLineWriter().writeLine(id + "");
  }

  private void getByid() {
    getLineWriter().writeLine("Enter author id:");
    long id = getUserInputReader().readLongFromLine();
    Author author = authorDao.getById(id);
    getLineWriter().writeLine("Result:");
    authorPrinter.print(author);
  }

  private void deleteById() {
    getLineWriter().writeLine("Enter author id:");
    long id = getUserInputReader().readLongFromLine();
    authorDao.deleteById(id);
    getLineWriter().writeLine("Deleted successfully");
  }

}
