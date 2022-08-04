package com.study.spring.library.shell;

import com.study.spring.library.domain.Book;
import com.study.spring.library.domain.Comment;
import com.study.spring.library.exceptions.EntityNotFoundException;
import com.study.spring.library.exceptions.UnsupportedValueException;
import com.study.spring.library.io.LineWriter;
import com.study.spring.library.io.Printer;
import com.study.spring.library.io.UserInputReader;
import com.study.spring.library.service.BookService;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.persistence.PersistenceException;
import org.springframework.stereotype.Component;

@Component
public class BookUserApi extends BaseUserApi {

  private static final String[] OPTIONS = new String[BookService.class.getDeclaredMethods().length];
  static {
    Arrays.stream(BookService.class.getDeclaredMethods())
          .map(Method::getName)
          .sorted()
          .collect(Collectors.toList())
          .toArray(OPTIONS);
  }

  private final BookService bookService;
  private final Printer<Book> bookPrinter;
  private final Printer<Comment> commentPrinter;

  public BookUserApi(
      UserInputReader userInputReader,
      LineWriter lineWriter,
      BookService bookService,
      Printer<Book> bookPrinter,
      Printer<Comment> commentPrinter) {

    super(userInputReader, lineWriter);
    this.bookService = bookService;
    this.bookPrinter = bookPrinter;
    this.commentPrinter = commentPrinter;
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
      case "getByTitle":
        getByTitle();
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
    String genre = requestGenre();
    String author = requestAuthor();
    bookService.update(book, genre, author);
    getLineWriter().writeLine("Book updated");
  }

  private void create() {
    Book book = gatherBookData(null);
    String genre = requestGenre();
    String author = requestAuthor();
    bookService.create(book, genre, author);
    getLineWriter().writeLine("Book created");
  }

  private Book gatherBookData(Long id) {
    getLineWriter().writeLine("Enter book title:");
    String title = getUserInputReader().readLine();
    getLineWriter().writeLine("Enter book description:");
    String description = getUserInputReader().readLine();
    return Book.builder()
               .id(id)
               .title(title)
               .description(description)
               .build();
  }

  private String requestGenre() {
    getLineWriter().writeLine("Enter book genre:");
    return getUserInputReader().readLine();
  }

  private String requestAuthor() {
    getLineWriter().writeLine("Enter book author:");
    return getUserInputReader().readLine();
  }

  private void getAll() {
    getLineWriter().writeLine("All books:");
    bookPrinter.print(bookService.getAll());
  }

  private void getByid() {
    getLineWriter().writeLine("Enter book id:");
    long id = getUserInputReader().readLongFromLine();
    Book book = bookService.getById(id);
    bookPrinter.print(book);
    getLineWriter().writeLine("Book comments:");
    commentPrinter.print(book.getComments());
  }

  private void getByTitle() {
    getLineWriter().writeLine("Enter book title:");
    String title = getUserInputReader().readLine();
    Book book = bookService.getByTitle(title);
    bookPrinter.print(book);
    getLineWriter().writeLine("Book comments:");
    commentPrinter.print(book.getComments());
  }

  private void deleteById() {
    getLineWriter().writeLine("Enter book id:");
    long id = getUserInputReader().readLongFromLine();
    bookService.deleteById(id);
    getLineWriter().writeLine("Deleted successfully");
  }

}
