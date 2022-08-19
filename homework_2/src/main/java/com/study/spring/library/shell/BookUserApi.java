package com.study.spring.library.shell;

import com.study.spring.library.constants.Options;
import com.study.spring.library.domain.Book;
import com.study.spring.library.domain.Comment;
import com.study.spring.library.exceptions.EntityNotFoundException;
import com.study.spring.library.exceptions.UnsupportedValueException;
import com.study.spring.library.io.LineWriter;
import com.study.spring.library.io.Printer;
import com.study.spring.library.io.UserInputReader;
import com.study.spring.library.service.BookService;
import org.springframework.stereotype.Component;

@Component
public class BookUserApi extends BaseUserApi {

  private static final String[] OPTIONS = {
      Options.CREATE,
      Options.DELETE_BY_ID,
      Options.GET_ALL,
      Options.GET_BY_ID,
      Options.GET_BY_TITLE,
      Options.UPDATE
  };

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
    } catch (RuntimeException e) {
      getLineWriter().writeLine(String.format("Error executing operation %s", e.getMessage()));
    }
  }

  @Override
  protected void chooseOperation(String operation) {
    switch (operation) {
      case Options.CREATE -> create();
      case Options.DELETE_BY_ID -> deleteById();
      case Options.GET_ALL -> getAll();
      case Options.GET_BY_ID -> getByid();
      case Options.GET_BY_TITLE -> getByTitle();
      case Options.UPDATE -> update();
      default -> throw new UnsupportedValueException(String.format("Unsupported option requested: %s", operation));
    }
  }

  private void update() {
    getLineWriter().writeLine("Enter book id:");
    String id = getUserInputReader().readLine();
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

  private Book gatherBookData(String id) {
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
    String id = getUserInputReader().readLine();
    Book book = bookService.findById(id);
    bookPrinter.print(book);
    commentPrinter.print(book.getComments());
  }

  private void getByTitle() {
    getLineWriter().writeLine("Enter book title:");
    String title = getUserInputReader().readLine();
    Book book = bookService.findByTitle(title);
    bookPrinter.print(book);
    commentPrinter.print(book.getComments());
  }

  private void deleteById() {
    getLineWriter().writeLine("Enter book id:");
    String id = getUserInputReader().readLine();
    bookService.deleteById(id);
    getLineWriter().writeLine("Deleted successfully");
  }

}
