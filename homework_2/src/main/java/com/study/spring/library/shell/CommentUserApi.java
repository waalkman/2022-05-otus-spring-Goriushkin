package com.study.spring.library.shell;

import com.study.spring.library.constants.Options;
import com.study.spring.library.domain.Comment;
import com.study.spring.library.exceptions.EntityNotFoundException;
import com.study.spring.library.exceptions.UnsupportedValueException;
import com.study.spring.library.io.LineWriter;
import com.study.spring.library.io.Printer;
import com.study.spring.library.io.UserInputReader;
import com.study.spring.library.service.CommentService;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CommentUserApi extends BaseUserApi {

  private static final String[] OPTIONS = {
      Options.CREATE,
      Options.DELETE_BY_ID,
      Options.GET_BY_BOOK_TITLE,
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
    } catch (RuntimeException e) {
      getLineWriter().writeLine(String.format("Error executing operation %s", e.getMessage()));
    }
  }

  @Override
  @Transactional
  protected void chooseOperation(String operation) {
    switch (operation) {
      case Options.CREATE -> create();
      case Options.DELETE_BY_ID -> deleteById();
      case Options.GET_BY_BOOK_TITLE -> findByBookTitle();
      case Options.GET_BY_ID -> getByid();
      case Options.UPDATE -> update();
      default -> throw new UnsupportedValueException(String.format("Unsupported option requested: %s", operation));
    }
  }

  private void update() {
    getLineWriter().writeLine("Enter comment id");
    String id = getUserInputReader().readLine();
    Comment comment = gatherCommentData(id);
    String book = requestBook();
    commentService.update(comment, book);
    getLineWriter().writeLine("Comment updated");
  }

  private void create() {
    Comment comment = gatherCommentData(new ObjectId().toString());
    String book = requestBook();
    commentService.create(comment, book);
    getLineWriter().writeLine("Comment created");
  }

  private void findByBookTitle() {
    String title = requestBook();
    printer.print(commentService.findByBookTitle(title));
  }

  private void getByid() {
    String title = requestBook();
    getLineWriter().writeLine("Enter comment id:");
    String id = getUserInputReader().readLine();
    Comment comment = commentService.findById(title, id);
    printer.print(comment);
  }

  private void deleteById() {
    String title = requestBook();
    getLineWriter().writeLine("Enter comment id:");
    String id = getUserInputReader().readLine();
    commentService.deleteById(title, id);
    getLineWriter().writeLine("Deleted successfully");
  }

  private Comment gatherCommentData(String id) {
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
