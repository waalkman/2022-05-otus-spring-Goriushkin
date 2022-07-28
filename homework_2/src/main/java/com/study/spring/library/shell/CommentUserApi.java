package com.study.spring.library.shell;

import com.study.spring.library.dao.BookDao;
import com.study.spring.library.dao.CommentDao;
import com.study.spring.library.domain.Book;
import com.study.spring.library.domain.Comment;
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
public class CommentUserApi extends BaseUserApi {

  private static final String[] OPTIONS = {"create", "deleteById", "getAll", "getById", "update"};

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
      getLineWriter().writeLine(String.format("%s not found", ex.getEntity()));
    } catch (DataAccessException e) {
      getLineWriter().writeLine(String.format("Error executing operation %s", e.getMessage()));
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
      case "update" -> update();
      default -> throw new UnsupportedValueException(String.format("Unsupported option requested: %s", operation));
    }
  }

  private void update() {
    getLineWriter().writeLine("Enter comment id");
    long id = getUserInputReader().readLongFromLine();
    commentDao.getById(id);
    Comment comment = gatherCommentData(id);
    commentDao.save(comment);
    getLineWriter().writeLine("Comment updated");
  }

  private void create() {
    Comment comment = gatherCommentData(null);
    commentDao.save(comment);
    getLineWriter().writeLine("Comment created");
  }

  private void getAll() {
    getLineWriter().writeLine("All comments:");
    printer.print(commentDao.findAll());
  }

  private void getByid() {
    getLineWriter().writeLine("Enter comment id:");
    long id = getUserInputReader().readLongFromLine();
    Optional<Comment> comment = commentDao.findById(id);
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
    Book book = bookDao.findByTitle(title)
                       .orElseThrow(() -> new EntityNotFoundException("Book not found", "Book"));

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
