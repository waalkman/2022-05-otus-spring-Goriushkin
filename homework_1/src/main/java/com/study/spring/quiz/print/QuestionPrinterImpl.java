package com.study.spring.quiz.print;

import com.study.spring.quiz.Constants;
import com.study.spring.quiz.dto.Option;
import com.study.spring.quiz.dto.Question;
import com.study.spring.quiz.io.LineWriter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionPrinterImpl implements QuestionPrinter {

  private final LineWriter lineWriter;

  @Override
  public void printQuestion(Question question) {
    lineWriter.writeLine(Constants.QUESTION_DELIMITER);
    lineWriter.writeLine(question.getTitle());
    printOptions(question);
    lineWriter.writeLine(Constants.QUESTION_DELIMITER);
  }

  private void printOptions(Question question) {
    if (!question.isFreeTextAnswer()) {
      List<Option> options = question.getOptions();
      for (int i = 1; i <= options.size(); i++) {
        lineWriter.writeLine(String.format("%d: %s", i, options.get(i - 1).getText()));
      }
    }
  }
}
