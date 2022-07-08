package com.study.spring.library.shell;

import com.study.spring.library.io.LineWriter;
import com.study.spring.library.io.UserInputReaderImpl;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseUserApi {

  protected final UserInputReaderImpl userInputReader;
  protected final LineWriter lineWriter;

  public int chooseOptionMenu() {
    lineWriter.writeLine("Choose option:");
    getOptions().forEach((key, value) -> lineWriter.writeLine(String.format("%d: %s", key, value)));
    return userInputReader.readIntFromLine();
  }

  public void acceptOption(int option) {
    String operation = getOptions().get(option);
    if (operation == null) {
      lineWriter.writeLine("Entered option index is out of range");
      return;
    }

    runOperation(operation);
  }

  protected abstract void runOperation(String operation);
  protected abstract void chooseOperation(String operation);
  protected abstract Map<Integer, String> getOptions();

}
