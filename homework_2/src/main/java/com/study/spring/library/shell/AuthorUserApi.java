package com.study.spring.library.shell;

import com.study.spring.library.dao.AuthorDao;
import com.study.spring.library.domain.Author;
import com.study.spring.library.exceptions.EntityNotFoundException;
import com.study.spring.library.exceptions.UnsupportedValueException;
import com.study.spring.library.io.LineWriter;
import com.study.spring.library.io.Printer;
import com.study.spring.library.io.UserInputReader;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AuthorUserApi extends BaseUserApi {

  private static final String[] OPTIONS = {"create", "deleteById", "getAll", "getById", "getByName", "update"};

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
    } catch (DataAccessException e) {
      getLineWriter().writeLine(String.format("Error executing operation %s", e.getCause().getCause().getMessage()));
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
    getLineWriter().writeLine("Enter author id:");
    long id = getUserInputReader().readLongFromLine();
    getLineWriter().writeLine("Enter author name:");
    String name = getUserInputReader().readLine();
    authorDao.save(new Author(id, name));
    getLineWriter().writeLine("Author updated");
  }

  private void create() {
    getLineWriter().writeLine("Enter new author name:");
    String name = getUserInputReader().readLine();
    authorDao.save(Author.builder().name(name).build());
    getLineWriter().writeLine("Author created");
  }

  private void getAll() {
    getLineWriter().writeLine("All authors:");
    authorPrinter.print(authorDao.findAll());
  }

  private void getIdByName() {
    getLineWriter().writeLine("Enter author name:");
    String name = getUserInputReader().readLine();
    authorPrinter.print(authorDao.findByName(name));
  }

  private void getByid() {
    getLineWriter().writeLine("Enter author id:");
    long id = getUserInputReader().readLongFromLine();
    Optional<Author> author = authorDao.findById(id);
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
