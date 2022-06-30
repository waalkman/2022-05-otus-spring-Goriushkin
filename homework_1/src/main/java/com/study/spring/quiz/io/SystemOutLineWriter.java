package com.study.spring.quiz.io;

import java.io.PrintStream;
import org.springframework.stereotype.Component;

@Component
public class SystemOutLineWriter implements LineWriter {

  private final PrintStream printStream = new PrintStream(System.out);

  @Override
  public void writeLine(String line) {
    printStream.println(line);
  }
}
