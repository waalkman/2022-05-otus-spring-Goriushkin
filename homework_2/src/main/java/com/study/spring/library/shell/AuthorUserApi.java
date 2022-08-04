package com.study.spring.library.shell;

import com.study.spring.library.domain.Author;
import com.study.spring.library.exceptions.EntityNotFoundException;
import com.study.spring.library.exceptions.UnsupportedValueException;
import com.study.spring.library.io.LineWriter;
import com.study.spring.library.io.Printer;
import com.study.spring.library.io.UserInputReader;
import com.study.spring.library.service.AuthorService;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.persistence.PersistenceException;
import org.springframework.stereotype.Component;

@Component
public class AuthorUserApi extends BaseUserApi {

  private static final String[] OPTIONS = new String[AuthorService.class.getDeclaredMethods().length];
  static {
    Arrays.stream(AuthorService.class.getDeclaredMethods())
          .map(Method::getName)
          .sorted()
          .collect(Collectors.toList())
          .toArray(OPTIONS);
  }

  private final AuthorService authorService;
  private final Printer<Author> authorPrinter;

  public AuthorUserApi(
      UserInputReader userInputReader,
      LineWriter lineWriter,
      AuthorService authorService,
      Printer<Author> authorPrinter) {

    super(userInputReader, lineWriter);
    this.authorService = authorService;
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
    getLineWriter().writeLine("Enter author id:");
    long id = getUserInputReader().readLongFromLine();
    getLineWriter().writeLine("Enter author name:");
    String name = getUserInputReader().readLine();
    authorService.update(new Author(id, name));
    getLineWriter().writeLine("Author updated");
  }

  private void create() {
    getLineWriter().writeLine("Enter new author name:");
    String name = getUserInputReader().readLine();
    authorService.create(Author.builder().name(name).build());
    getLineWriter().writeLine("Author created");
  }

  private void getAll() {
    getLineWriter().writeLine("All authors:");
    authorPrinter.print(authorService.getAll());
  }

  private void getIdByName() {
    getLineWriter().writeLine("Enter author name:");
    String name = getUserInputReader().readLine();
    authorPrinter.print(authorService.getByName(name));
  }

  private void getByid() {
    getLineWriter().writeLine("Enter author id:");
    long id = getUserInputReader().readLongFromLine();
    Author author = authorService.getById(id);
    getLineWriter().writeLine("Result:");
    authorPrinter.print(author);
  }

  private void deleteById() {
    getLineWriter().writeLine("Enter author id:");
    long id = getUserInputReader().readLongFromLine();
    authorService.deleteById(id);
    getLineWriter().writeLine("Deleted successfully");
  }

}
