package com.study.spring.quiz.shell;

import com.study.spring.quiz.AppRunner;
import com.study.spring.quiz.exam.UserNameHolder;
import com.study.spring.quiz.io.LineWriter;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@RequiredArgsConstructor
public class QuizShell {

  private final AppRunner appRunner;
  private final UserNameHolder userNameHolder;
  private final LineWriter lineWriter;
  private final MessageSource messageSource;
  private final CommandPrerequisitesChecker prerequisitesChecker;

  @ShellMethod(key = "test", value = "Run student test")
  public void runTests() {
    if (prerequisitesChecker.canRunTests()) {
      appRunner.run();
    }
  }

  @ShellMethod(key = "login", value = "Login with your name and surname")
  public void askName(@ShellOption String name) {
    userNameHolder.setName(name);
    lineWriter.writeLine(messageSource.getMessage("greeting", new String[]{name}, Locale.getDefault()));
  }

}
