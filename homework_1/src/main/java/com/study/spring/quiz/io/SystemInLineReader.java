package com.study.spring.quiz.io;

import java.util.Scanner;
import org.springframework.stereotype.Component;

@Component
public class SystemInLineReader implements LineReader {

  private final Scanner scanner = new Scanner(System.in);

  @Override
  public String readLine() {
    return scanner.nextLine();
  }
}
