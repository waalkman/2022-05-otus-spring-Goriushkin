package com.study.spring.library.io;

import com.study.spring.library.domain.Genre;
import org.springframework.stereotype.Component;

@Component
public class GenrePrinter extends Printer<Genre> {

  private static final String HEADER = "ID" + " ".repeat(28) + "GENRE";
  private static final String PATTERN = "%-30s%s";

  public GenrePrinter(LineWriter lineWriter) {
    super(lineWriter);
  }

  @Override
  protected void printSingleEntity(Genre genre) {
    lineWriter.writeLine(String.format(PATTERN, genre.getId(), genre.getName()));
  }

  @Override
  protected void printHeader() {
    lineWriter.writeLine(HEADER);
  }

}
