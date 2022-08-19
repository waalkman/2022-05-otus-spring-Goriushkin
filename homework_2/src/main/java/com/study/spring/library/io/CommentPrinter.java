package com.study.spring.library.io;

import com.study.spring.library.domain.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentPrinter extends Printer<Comment> {

  private static final String HEADER = "ID" + " ".repeat(28) + "TEXT" + " ".repeat(45) + "USER_NAME";

  private static final String PATTERN = "%-30s%-49s%s";

  public CommentPrinter(LineWriter lineWriter) {
    super(lineWriter);
  }

  @Override
  protected void printSingleEntity(Comment comment) {
    lineWriter.writeLine(
        String.format(
            PATTERN,
            comment.getId(),
            comment.getText(),
            comment.getUserName()));
  }

  @Override
  protected void printHeader() {
    lineWriter.writeLine(HEADER);
  }

}
