package com.study.spring.library.io;

import com.study.spring.library.domain.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorPrinter extends Printer<Author> {

  private static final String HEADER = "ID\tNAME";
  private static final String PATTERN = "%-4d%s";

  public AuthorPrinter(LineWriter lineWriter) {
    super(lineWriter);
  }

  @Override
  protected void printHeader() {
    lineWriter.writeLine(HEADER);
  }

  @Override
  protected void printSingleEntity(Author author) {
    lineWriter.writeLine(String.format(PATTERN, author.getId(), author.getName()));
  }
}
