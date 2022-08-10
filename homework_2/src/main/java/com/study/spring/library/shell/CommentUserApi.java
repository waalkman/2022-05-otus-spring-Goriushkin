package com.study.spring.library.shell;

import com.study.spring.library.domain.Comment;
import com.study.spring.library.exceptions.EntityNotFoundException;
import com.study.spring.library.exceptions.UnsupportedValueException;
import com.study.spring.library.io.LineWriter;
import com.study.spring.library.io.Printer;
import com.study.spring.library.io.UserInputReader;
import com.study.spring.library.service.CommentService;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.persistence.PersistenceException;
import liquibase.repackaged.org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

@Component
public class CommentUserApi extends BaseUserApi {

  private static final String[] OPTIONS = new String[CommentService.class.getDeclaredMethods().length];

  static {
    Arrays.stream(CommentService.class.getDeclaredMethods())
          .map(Method::getName)
          .sorted()
          .collect(Collectors.toList())
          .toArray(OPTIONS);
  }

  private final CommentService commentService;
  private final Printer<Comment> printer;

  public CommentUserApi(
      UserInputReader userInputReader,
      LineWriter lineWriter,
      CommentService commentService,
      Printer<Comment> printer) {

    super(userInputReader, lineWriter);
    this.commentService = commentService;
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
    } catch (PersistenceException e) {
      getLineWriter().writeLine(String.format("Error executing operation %s", e.getMessage()));
      ExceptionUtils.printRootCauseStackTrace(e);
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
    Comment comment = gatherCommentData(id);
    String book = requestBook();
    commentService.update(comment, book);
    getLineWriter().writeLine("Comment updated");
  }

  private void create() {
    Comment comment = gatherCommentData(null);
    String book = requestBook();
    commentService.create(comment, book);
    getLineWriter().writeLine("Comment created");
  }

  private void getAll() {
    getLineWriter().writeLine("All comments:");
    printer.print(commentService.getAll());
  }

  private void getByid() {
    getLineWriter().writeLine("Enter comment id:");
    long id = getUserInputReader().readLongFromLine();
    Comment comment = commentService.getById(id);
    getLineWriter().writeLine("Result:");
    printer.print(comment);
  }

  private void deleteById() {
    getLineWriter().writeLine("Enter comment id:");
    long id = getUserInputReader().readLongFromLine();
    commentService.deleteById(id);
    getLineWriter().writeLine("Deleted successfully");
  }

  private Comment gatherCommentData(Long id) {
    getLineWriter().writeLine("Enter your comment:");
    String comment = getUserInputReader().readLine();
    getLineWriter().writeLine("Enter your username:");
    String username = getUserInputReader().readLine();

    return Comment.builder()
                  .id(id)
                  .userName(username)
                  .text(comment)
                  .build();
  }

  private String requestBook() {
    getLineWriter().writeLine("Enter book title:");
    return getUserInputReader().readLine();
  }

}
