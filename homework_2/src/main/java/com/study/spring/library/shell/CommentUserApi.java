package com.study.spring.library.shell;

import com.study.spring.library.dao.BookDao;
import com.study.spring.library.dao.CommentDao;
import com.study.spring.library.domain.Book;
import com.study.spring.library.domain.Comment;
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
public class CommentUserApi extends BaseUserApi {

  private static final String[] OPTIONS = new String[CommentDao.class.getDeclaredMethods().length];

  static {
    Arrays.stream(CommentDao.class.getDeclaredMethods())
          .map(Method::getName)
          .sorted()
          .collect(Collectors.toList())
          .toArray(OPTIONS);
  }

  private final CommentDao commentDao;
  private final BookDao bookDao;
  private final Printer<Comment> printer;

  public CommentUserApi(
      UserInputReader userInputReader,
      LineWriter lineWriter,
      CommentDao commentDao,
      BookDao bookDao,
      Printer<Comment> printer) {

    super(userInputReader, lineWriter);
    this.commentDao = commentDao;
    this.bookDao = bookDao;
    this.printer = printer;
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
      getLineWriter().writeLine("Comment(s) not found");
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
    getLineWriter().writeLine("Enter comment id");
    long id = getUserInputReader().readLongFromLine();
    commentDao.getById(id);
    Comment comment = gatherCommentData(id);
    commentDao.create(comment);
    getLineWriter().writeLine("Comment updated");
  }

  private void create() {
    Comment comment = gatherCommentData(null);
    commentDao.create(comment);
    getLineWriter().writeLine("Comment created");
  }

  private void getAll() {
    getLineWriter().writeLine("All comments:");
    printer.print(commentDao.getAll());
  }

  private void getByid() {
    getLineWriter().writeLine("Enter comment id:");
    long id = getUserInputReader().readLongFromLine();
    Comment comment = commentDao.getById(id);
    getLineWriter().writeLine("Result:");
    printer.print(comment);
  }

  private void deleteById() {
    getLineWriter().writeLine("Enter comment id:");
    long id = getUserInputReader().readLongFromLine();
    commentDao.deleteById(id);
    getLineWriter().writeLine("Deleted successfully");
  }

  private Comment gatherCommentData(Long id) {
    getLineWriter().writeLine("Enter book title:");
    String title = getUserInputReader().readLine();
    Book book = bookDao.getByTitle(title);
    getLineWriter().writeLine("Enter your comment:");
    String comment = getUserInputReader().readLine();
    getLineWriter().writeLine("Enter your username:");
    String username = getUserInputReader().readLine();

    return Comment.builder()
                  .id(id)
                  .book(book)
                  .userName(username)
                  .text(comment)
                  .build();
  }

}
