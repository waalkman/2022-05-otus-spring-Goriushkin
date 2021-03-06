package com.study.spring.library.shell;

import com.study.spring.library.dao.AuthorDao;
import com.study.spring.library.dao.BookDao;
import com.study.spring.library.dao.GenreDao;
import com.study.spring.library.domain.Book;
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
public class BookUserApi extends BaseUserApi {

  private static final String[] OPTIONS = new String[BookDao.class.getDeclaredMethods().length];
  static {
    Arrays.stream(BookDao.class.getDeclaredMethods())
          .map(Method::getName)
          .sorted()
          .collect(Collectors.toList())
          .toArray(OPTIONS);
  }

  private final BookDao bookDao;
  private final AuthorDao authorDao;
  private final GenreDao genreDao;
  private final Printer<Book> bookPrinter;

  public BookUserApi(
      UserInputReader userInputReader,
      LineWriter lineWriter,
      BookDao bookDao,
      AuthorDao authorDao,
      GenreDao genreDao,
      Printer<Book> bookPrinter) {

    super(userInputReader, lineWriter);
    this.authorDao = authorDao;
    this.genreDao = genreDao;
    this.bookDao = bookDao;
    this.bookPrinter = bookPrinter;
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
      getLineWriter().writeLine(String.format("%s not found", ex.getEntity()));
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
      case "deleteById":
        deleteById();
        break;
      default:
        throw new UnsupportedValueException(String.format("Unsupported option requested: %s", operation));
    }
  }

  private void update() {
    getLineWriter().writeLine("Enter book id:");
    long id = getUserInputReader().readLongFromLine();
    Book book = gatherBookData(id);
    Long genreId = genreDao.getIdByName(book.getGenre());
    Long authorId = authorDao.getIdByName(book.getAuthor());
    bookDao.update(book, genreId, authorId);
    getLineWriter().writeLine("Book updated");
  }

  private void create() {
    Book book = gatherBookData(null);
    Long genreId = genreDao.getIdByName(book.getGenre());
    Long authorId = authorDao.getIdByName(book.getAuthor());
    bookDao.create(book, genreId, authorId);
    getLineWriter().writeLine("Book created");
  }

  private Book gatherBookData(Long id) {
    getLineWriter().writeLine("Enter book title:");
    String title = getUserInputReader().readLine();
    getLineWriter().writeLine("Enter book description:");
    String description = getUserInputReader().readLine();
    getLineWriter().writeLine("Enter book genre:");
    String genre = getUserInputReader().readLine();
    getLineWriter().writeLine("Enter book author:");
    String author = getUserInputReader().readLine();
    return Book.builder()
               .id(id)
               .title(title)
               .description(description)
               .genre(genre)
               .author(author)
               .build();
  }

  private void getAll() {
    getLineWriter().writeLine("All books:");
    bookPrinter.print(bookDao.getAll());
  }

  private void getByid() {
    getLineWriter().writeLine("Enter book id:");
    long id = getUserInputReader().readLongFromLine();
    Book book = bookDao.getById(id);
    bookPrinter.print(book);
  }

  private void deleteById() {
    getLineWriter().writeLine("Enter book id:");
    long id = getUserInputReader().readLongFromLine();
    bookDao.deleteById(id);
    getLineWriter().writeLine("Deleted successfully");
  }

}
