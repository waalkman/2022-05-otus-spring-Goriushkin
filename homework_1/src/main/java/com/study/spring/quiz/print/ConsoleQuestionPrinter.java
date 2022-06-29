package com.study.spring.quiz.print;

import com.study.spring.quiz.Constants;
import com.study.spring.quiz.dto.Option;
import com.study.spring.quiz.dto.Question;
import java.io.PrintStream;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ConsoleQuestionPrinter implements QuestionPrinter {

  private final PrintStream printStream = new PrintStream(System.out);

  @Override
  public void printQuestion(Question question) {
    printLine(Constants.QUESTION_DELIMITER);
    printLine(question.getTitle());
    printOptions(question);
    printLine(Constants.QUESTION_DELIMITER);
  }

  private void printLine(String line) {
    printStream.println(line);
  }

  private void printOptions(Question question) {
    if (!question.isFreeTextAnswer()) {
      List<Option> options = question.getOptions();
      for (int i = 1; i <= options.size(); i++) {
        printLine(String.format("%d: %s", i, options.get(i - 1).getText()));
      }
    }
  }
}
