package com.study.spring.quiz.exam;

import com.study.spring.quiz.print.QuestionPrinter;
import java.io.PrintStream;
import java.util.Scanner;
import org.springframework.stereotype.Component;

@Component
public class SystemInOutExaminer extends BaseExaminer {

  private final PrintStream printStream = new PrintStream(System.out);
  private final Scanner scanner = new Scanner(System.in);

  public SystemInOutExaminer(
      QuestionPrinter questionPrinter,
      AnswerChecker answerChecker,
      AnswerFormatValidator answerFormatValidator) {

    super(questionPrinter, answerChecker, answerFormatValidator);
  }

  @Override
  protected void printLine(String line) {
    printStream.println(line);
  }

  @Override
  protected String readLine() {
    return scanner.nextLine();
  }
}
