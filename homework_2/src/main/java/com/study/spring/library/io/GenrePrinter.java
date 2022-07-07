package com.study.spring.library.io;

import com.study.spring.library.domain.Genre;
import java.util.Collection;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

@Component
public class GenrePrinter extends Printer<Genre> {

  private static final String HEADER = "ID\tGENRE";
  private static final String PATTERN = "%-4d%s";

  public GenrePrinter(LineWriter lineWriter) {
    super(lineWriter);
  }

  @Override
  public void print(@NotNull Genre genre) {
    lineWriter.writeLine(HEADER);
    lineWriter.writeLine(String.format(PATTERN, genre.getId(), genre.getName()));
  }

  @Override
  public void print(@NotNull Collection<Genre> genres) {
    lineWriter.writeLine(HEADER);
    genres.forEach(author -> lineWriter.writeLine(String.format(PATTERN, author.getId(), author.getName())));
  }
}
