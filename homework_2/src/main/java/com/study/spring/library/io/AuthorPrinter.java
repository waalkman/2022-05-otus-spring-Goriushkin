package com.study.spring.library.io;

import com.study.spring.library.domain.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorPrinter extends Printer<Author> {

  private static final String HEADER = "ID" + " ".repeat(28) + "NAME";
  private static final String PATTERN = "%-30s%s";

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
