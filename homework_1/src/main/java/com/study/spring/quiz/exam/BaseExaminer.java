package com.study.spring.quiz.exam;

import com.study.spring.quiz.dto.Question;
import com.study.spring.quiz.dto.QuestionResult;
import com.study.spring.quiz.exceptions.IncorrectAnswerException;
import com.study.spring.quiz.print.QuestionPrinter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@RequiredArgsConstructor
public abstract class BaseExaminer implements Examiner {

  private final QuestionPrinter questionPrinter;
  private final AnswerChecker answerChecker;
  private final AnswerFormatValidator answerFormatValidator;

  @Value("${test.pass.threshold}")
  private String testPassThreshold;
  @Value("${test.input.retry.threshold}")
  private String testInputRetryThreshold;

  @Override
  public void examStudent(List<Question> questions) {
    printGreeting();
    String name = readLine();
    List<QuestionResult> testResult = executeTest(questions);
    long correctAnswerCount = countCorrectAnswers(testResult);
    printResult(correctAnswerCount, name);
  }

  protected void printGreeting() {
    printLine("For optioned questions print your response separated by space!");
    printLine("Enter your name and surname:");
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

  protected abstract void printLine(String line);

  protected abstract String readLine();

  private String tryGetUserAnswer(Question question) {
    int retries = Integer.parseInt(testInputRetryThreshold);
    while (retries > 0) {
      try {
        String userAnswer = readLine();
        answerFormatValidator.validateAnswerFormat(question, userAnswer);
        return userAnswer;
      } catch (IncorrectAnswerException e) {
        printLine(String.format("Incorrect input: %s. try again", e.getMessage()));
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
    if (correctAnswerCount >= Integer.parseInt(testPassThreshold)) {
      printLine(String.format("Congratulations %s! You have passed the test", name));
    } else {
      printLine(String.format("Congratulations %s! You have failed the test", name));
    }
  }
}
