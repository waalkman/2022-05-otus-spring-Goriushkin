package com.study.spring.library.io;

import com.study.spring.library.domain.Author;
import java.util.Collection;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

@Component
public class AuthorPrinter extends Printer<Author> {

  private static final String HEADER = "ID\tNAME";
  private static final String PATTERN = "%-4d%s";

  public AuthorPrinter(LineWriter lineWriter) {
    super(lineWriter);
  }

  @Override
  public void print(@NotNull Author author) {
    lineWriter.writeLine(HEADER);
    lineWriter.writeLine(String.format(PATTERN, author.getId(), author.getName()));
  }

  @Override
  public void print(@NotNull Collection<Author> authors) {
    lineWriter.writeLine(HEADER);
    authors.forEach(author -> lineWriter.writeLine(String.format(PATTERN, author.getId(), author.getName())));
  }
}
