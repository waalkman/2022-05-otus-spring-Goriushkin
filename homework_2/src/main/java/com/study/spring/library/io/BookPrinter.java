package com.study.spring.library.io;

import com.study.spring.library.domain.Book;
import org.springframework.stereotype.Component;

@Component
public class BookPrinter extends Printer<Book> {

  private static final String HEADER =
      "ID  TITLE" + " ".repeat(45) + "DESCRIPTION" + " ".repeat(39) + "AUTHOR_ID" + " ".repeat(4) + "GENRE_ID";

  private static final String PATTERN = "%-4d%-50s%-50s%-13d%-4d";

  public BookPrinter(LineWriter lineWriter) {
    super(lineWriter);
  }

  @Override
  protected void printSingleEntity(Book book) {
    lineWriter.writeLine(
        String.format(
            PATTERN, book.getId(), book.getTitle(), book.getDescription(), book.getAuthorId(), book.getGenreId()));
  }

  @Override
  protected void printHeader() {
    lineWriter.writeLine(HEADER);
  }
}
