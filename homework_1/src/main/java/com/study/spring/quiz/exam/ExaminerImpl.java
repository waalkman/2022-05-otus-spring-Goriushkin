package com.study.spring.quiz.exam;

import com.study.spring.quiz.config.ExamParams;
import com.study.spring.quiz.dto.Question;
import com.study.spring.quiz.dto.QuestionResult;
import com.study.spring.quiz.exceptions.IncorrectAnswerException;
import com.study.spring.quiz.io.LineReader;
import com.study.spring.quiz.io.LineWriter;
import com.study.spring.quiz.print.QuestionPrinter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExaminerImpl implements Examiner {

  private final QuestionPrinter questionPrinter;
  private final AnswerChecker answerChecker;
  private final AnswerFormatValidator answerFormatValidator;
  private final MessageSource messageSource;
  private final LineReader lineReader;
  private final LineWriter lineWriter;
  private final UserNameHolder userNameHolder;
  private final ExamParams examParams;

  @Override
  public void examStudent(List<Question> questions) {
    printGreeting();
    List<QuestionResult> testResult = executeTest(questions);
    long correctAnswerCount = countCorrectAnswers(testResult);
    printResult(correctAnswerCount, userNameHolder.getName());
  }

  private void printGreeting() {
    lineWriter.writeLine(messageSource.getMessage("test.rules", null, Locale.getDefault()));
  }

  private List<QuestionResult> executeTest(List<Question> questions) {
    return questions.stream()
                    .peek(questionPrinter::printQuestion)
                    .map(question -> {
                      String userAnswer = tryGetUserAnswer(question);
                      return answerChecker.checkAnswer(question, userAnswer);
                    })
                    .collect(Collectors.toList());
  }

  private String tryGetUserAnswer(Question question) {
    int retries = examParams.getInputRetryThreshold();
    while (retries > 0) {
      try {
        String userAnswer = lineReader.readLine();
        answerFormatValidator.validateAnswerFormat(question, userAnswer);
        return userAnswer;
      } catch (IncorrectAnswerException e) {
        lineWriter.writeLine(String.format("Incorrect input: %s. try again", e.getMessage()));
        retries -= 1;
      }
    }
    throw new IncorrectAnswerException("Maximum number of typos exceeded");
  }

  private long countCorrectAnswers(List<QuestionResult> testResult) {
    return testResult.stream()
                     .filter(QuestionResult::isAnswerIsCorrect)
                     .count();
  }

  private void printResult(long correctAnswerCount, String name) {
    if (correctAnswerCount >= examParams.getPassThreshold()) {
      lineWriter.writeLine(messageSource.getMessage("success.test.result", new String[]{name}, Locale.getDefault()));
    } else {
      lineWriter.writeLine(messageSource.getMessage("failed.test.result", new String[]{name},  Locale.getDefault()));
    }
  }
}
