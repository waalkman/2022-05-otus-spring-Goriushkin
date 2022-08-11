package com.study.spring.library.shell;

import com.study.spring.library.constants.Options;
import com.study.spring.library.domain.Comment;
import com.study.spring.library.exceptions.EntityNotFoundException;
import com.study.spring.library.exceptions.UnsupportedValueException;
import com.study.spring.library.io.LineWriter;
import com.study.spring.library.io.Printer;
import com.study.spring.library.io.UserInputReader;
import com.study.spring.library.service.CommentService;
import liquibase.repackaged.org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CommentUserApi extends BaseUserApi {

  private static final String[] OPTIONS = {
      Options.CREATE,
      Options.DELETE_BY_ID,
      Options.GET_ALL,
      Options.GET_BY_ID,
      Options.UPDATE
  };

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
    } catch (DataAccessException e) {
      getLineWriter().writeLine(String.format("Error executing operation %s", e.getMessage()));
      ExceptionUtils.printRootCauseStackTrace(e);
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
      case Options.UPDATE -> update();
      default -> throw new UnsupportedValueException(String.format("Unsupported option requested: %s", operation));
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
    Comment comment = commentService.findById(id);
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
