package com.study.spring.library.shell;

import com.study.spring.library.dao.AuthorDao;
import com.study.spring.library.dao.BookDao;
import com.study.spring.library.dao.GenreDao;
import com.study.spring.library.domain.Book;
import com.study.spring.library.exceptions.UnsupportedValueException;
import com.study.spring.library.io.LineWriter;
import com.study.spring.library.io.UserInputReader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class BookUserApi extends BaseUserApi {

  private static final Map<Integer, String> OPTIONS = new HashMap<>();
  static {
    Method[] methods = BookDao.class.getDeclaredMethods();
    for (int i = 0; i < methods.length; ++i) {
      OPTIONS.put(i + 1, methods[i].getName());
    }
  }

  private final BookDao bookDao;
  private final GenreDao genreDao;
  private final AuthorDao authorDao;

  public BookUserApi(
      UserInputReader userInputReader,
      LineWriter lineWriter,
      BookDao bookDao,
      GenreDao genreDao, AuthorDao authorDao) {

    super(userInputReader, lineWriter);
    this.bookDao = bookDao;
    this.genreDao = genreDao;
    this.authorDao = authorDao;
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
      case "getByid":
        getByid();
        break;
      case "deleteById":
        deleteById();
        break;
      default:
        throw new UnsupportedValueException(String.format("Unsupported option requested: %s", operation));
    }
  }

  public void update() {
    lineWriter.writeLine("Enter book id:");
    long id = userInputReader.readLongFromLine();
    Book book = gatherBookData(id);
    bookDao.update(book);
    lineWriter.writeLine("Book updated");
  }

  public void create() {
    Book book = gatherBookData(null);
    bookDao.create(book);
    lineWriter.writeLine("Book created");
  }

  private Book gatherBookData(Long id) {
    lineWriter.writeLine("Enter book title:");
    String title = userInputReader.readLine();
    lineWriter.writeLine("Enter book description:");
    String description = userInputReader.readLine();
    lineWriter.writeLine("Enter book genre:");
    String genre = userInputReader.readLine();
    Long genreId = genreDao.getIdByName(genre);
    lineWriter.writeLine("Enter book author:");
    String author = userInputReader.readLine();
    Long authorId = authorDao.getIdByName(author);
    return Book.builder()
               .id(id)
               .title(title)
               .description(description)
               .genreId(genreId)
               .authorId(authorId)
               .build();
  }

  public void getAll() {
    lineWriter.writeLine("All books:");
    lineWriter.writeLine(bookDao.getAll().toString());
  }

  private void getByid() {
    lineWriter.writeLine("Enter book id:");
    long id = userInputReader.readLongFromLine();
    Book book = bookDao.getById(id);
    lineWriter.writeLine("Result:");
    lineWriter.writeLine(book.toString());
  }

  private void deleteById() {
    lineWriter.writeLine("Enter book id:");
    long id = userInputReader.readLongFromLine();
    bookDao.deleteById(id);
    lineWriter.writeLine("Deleted successfully");
  }

}
