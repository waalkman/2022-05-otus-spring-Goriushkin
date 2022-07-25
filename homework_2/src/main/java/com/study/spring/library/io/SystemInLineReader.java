package com.study.spring.library.io;

import java.util.Scanner;
import org.springframework.stereotype.Component;

@Component
public class SystemInLineReader implements LineReader {

  private final Scanner scanner = new Scanner(System.in);

  @Override
  public String readLine() {
    return scanner.nextLine();
  }

  @Override
  public int readIntFromLine() {
    String line = scanner.nextLine();
    return Integer.parseInt(line);
  }

  @Override
  public long readLongFromLine() {
    String line = scanner.nextLine();
    return Long.parseLong(line);
  }
}
