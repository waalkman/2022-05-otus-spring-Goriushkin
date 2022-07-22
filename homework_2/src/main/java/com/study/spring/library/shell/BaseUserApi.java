package com.study.spring.library.shell;

import com.study.spring.library.io.LineWriter;
import com.study.spring.library.io.UserInputReader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Getter
@RequiredArgsConstructor
public abstract class BaseUserApi {

  private final UserInputReader userInputReader;
  private final LineWriter lineWriter;

  @Transactional
  public void selectAndPerformOperation() {
    lineWriter.writeLine("Choose option:");
    printOptions();
    int option = userInputReader.readIntFromLine();
    if (option > getOptions().length) {
      lineWriter.writeLine("Entered option index is out of range");
      return;
    }
    String operation = getOptions()[option - 1];

    runOperation(operation);
  }

  protected abstract void runOperation(String operation);
  protected abstract void chooseOperation(String operation);
  protected abstract String[] getOptions();

  private void printOptions() {
    for (int i = 0; i < getOptions().length; ++i) {
      lineWriter.writeLine(String.format("%d: %s", i + 1, getOptions()[i]));
    }
  }

}
