package com.study.spring.library.io;

import com.study.spring.library.config.InputConfig;
import com.study.spring.library.exceptions.NotEnoughPatienceException;
import java.util.InputMismatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserInputReader {

  private final InputConfig inputConfig;
  private final LineWriter lineWriter;
  private final LineReader lineReader;

  public String readLine() {
    return lineReader.readLine();
  }

  public int readIntFromLine() {
    int retries = inputConfig.getRetriesThreshold();
    while (retries > 0) {
      try {
        return lineReader.readIntFromLine();
      } catch (NumberFormatException e) {
        lineWriter.writeLine("Not an integer! Try again");
        --retries;
      }
    }
    throw new NotEnoughPatienceException("Stop making typos! You are annoying me!!!!!");
  }

  public long readLongFromLine() {
    int retries = inputConfig.getRetriesThreshold();
    while (retries > 0) {
      try {
        return lineReader.readLongFromLine();
      } catch (InputMismatchException e) {
        lineWriter.writeLine("Not a long! Try again");
        --retries;
      }
    }
    throw new NotEnoughPatienceException("Stop making typos! You are annoying me!!!!!");
  }

}
