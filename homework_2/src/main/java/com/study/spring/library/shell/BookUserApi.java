package com.study.spring.library.shell;

import com.study.spring.library.dao.AuthorDao;
import com.study.spring.library.dao.BookDao;
import com.study.spring.library.dao.GenreDao;
import com.study.spring.library.domain.Book;
import com.study.spring.library.domain.PrintableBook;
import com.study.spring.library.exceptions.DataQueryException;
import com.study.spring.library.exceptions.EntityNotFoundException;
import com.study.spring.library.exceptions.UnsupportedValueException;
import com.study.spring.library.io.LineWriter;
import com.study.spring.library.io.Printer;
import com.study.spring.library.io.UserInputReader;
import com.study.spring.library.map.EntityMapper;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
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
  private final GenreDao genreDao;
  private final AuthorDao authorDao;
  private final EntityMapper<Book, PrintableBook> bookMapper;
  private final Printer<PrintableBook> bookPrinter;

  public BookUserApi(
      UserInputReader userInputReader,
      LineWriter lineWriter,
      BookDao bookDao,
      GenreDao genreDao,
      AuthorDao authorDao,
      EntityMapper<Book, PrintableBook> bookMapper,
      Printer<PrintableBook> bookPrinter) {

    super(userInputReader, lineWriter);
    this.bookDao = bookDao;
    this.genreDao = genreDao;
    this.authorDao = authorDao;
    this.bookMapper = bookMapper;
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
    bookDao.update(book);
    getLineWriter().writeLine("Book updated");
  }

  private void create() {
    Book book = gatherBookData(null);
    bookDao.create(book);
    getLineWriter().writeLine("Book created");
  }

  private Book gatherBookData(Long id) {
    getLineWriter().writeLine("Enter book title:");
    String title = getUserInputReader().readLine();
    getLineWriter().writeLine("Enter book description:");
    String description = getUserInputReader().readLine();
    getLineWriter().writeLine("Enter book genre:");
    String genre = getUserInputReader().readLine();
    Long genreId = genreDao.getIdByName(genre);
    getLineWriter().writeLine("Enter book author:");
    String author = getUserInputReader().readLine();
    Long authorId = authorDao.getIdByName(author);
    return Book.builder()
               .id(id)
               .title(title)
               .description(description)
               .genreId(genreId)
               .authorId(authorId)
               .build();
  }

  private void getAll() {
    getLineWriter().writeLine("All books:");
    Collection<PrintableBook> printableBooks =
        bookDao.getAll()
               .stream()
               .map(bookMapper::map)
               .collect(Collectors.toList());

    bookPrinter.print(printableBooks);
  }

  private void getByid() {
    getLineWriter().writeLine("Enter book id:");
    long id = getUserInputReader().readLongFromLine();
    Book book = bookDao.getById(id);
    bookPrinter.print(bookMapper.map(book));
  }

  private void deleteById() {
    getLineWriter().writeLine("Enter book id:");
    long id = getUserInputReader().readLongFromLine();
    bookDao.deleteById(id);
    getLineWriter().writeLine("Deleted successfully");
  }

}
