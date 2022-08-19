package com.study.spring.library.shell;

import com.study.spring.library.constants.Options;
import com.study.spring.library.domain.Author;
import com.study.spring.library.exceptions.EntityNotFoundException;
import com.study.spring.library.exceptions.UnsupportedValueException;
import com.study.spring.library.io.LineWriter;
import com.study.spring.library.io.Printer;
import com.study.spring.library.io.UserInputReader;
import com.study.spring.library.service.AuthorService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AuthorUserApi extends BaseUserApi {

  private static final String[] OPTIONS = {
      Options.CREATE,
      Options.DELETE_BY_ID,
      Options.GET_ALL,
      Options.GET_BY_ID,
      Options.GET_BY_NAME,
      Options.UPDATE
  };

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
    } catch (RuntimeException e) {
      getLineWriter().writeLine(String.format("Error executing operation %s", e.getMessage()));
    }
  }

  @Override
  @Transactional
  protected void chooseOperation(String operation) {
    switch (operation) {
      case Options.CREATE -> create();
      case Options.DELETE_BY_ID -> deleteById();
      case Options.GET_ALL -> getAll();
      case Options.GET_BY_ID -> getByid();
      case Options.GET_BY_NAME -> findByName();
      case Options.UPDATE -> update();
      default -> throw new UnsupportedValueException(String.format("Unsupported option requested: %s", operation));
    }
  }

  private void update() {
    getLineWriter().writeLine("Enter author id:");
    String id = getUserInputReader().readLine();
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

  private void findByName() {
    getLineWriter().writeLine("Enter author name:");
    String name = getUserInputReader().readLine();
    Author author = authorService.findByName(name);
    authorPrinter.print(author);
  }

  private void getByid() {
    getLineWriter().writeLine("Enter author id:");
    String id = getUserInputReader().readLine();
    Author author = authorService.findById(id);
    authorPrinter.print(author);
  }

  private void deleteById() {
    getLineWriter().writeLine("Enter author id:");
    String id = getUserInputReader().readLine();
    authorService.deleteById(id);
    getLineWriter().writeLine("Deleted successfully");
  }

}
