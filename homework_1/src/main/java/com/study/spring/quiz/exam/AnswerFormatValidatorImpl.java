package com.study.spring.quiz.exam;

import com.study.spring.quiz.dto.Question;
import com.study.spring.quiz.exceptions.IncorrectAnswerException;
import java.util.Arrays;
import org.springframework.stereotype.Component;

@Component
public class AnswerFormatValidatorImpl implements AnswerFormatValidator {

  @Override
  public void validateAnswerFormat(Question question, String userAnswer) {
    if (question.isFreeTextAnswer()) {
      validateFreetextAnswer(userAnswer);
    } else {
      validateOptionsAnswer(question, userAnswer);
    }
  }

  private void validateFreetextAnswer(String userAnswer) {
    if (userAnswer == null || userAnswer.isEmpty()) {
      throw new IncorrectAnswerException("Invalid freetext answer format");
    }
  }

  private void validateOptionsAnswer(Question question, String userAnswer) {
      String[] answers = userAnswer.split(" ", -1);
      parseAndCheckAnswers(question, answers);

    }

    private void parseAndCheckAnswers(Question question, String[] answers) {
      try {
        boolean inputIsOutOfRange =
            Arrays.stream(answers)
                  .map(Integer::parseInt)
                  .anyMatch(index -> index > question.getOptions().size() || index < 1);

        if (inputIsOutOfRange) {
          throw new IncorrectAnswerException("Invalid answer format. Entered index out of range");
        }
      } catch (NumberFormatException e) {
        throw new IncorrectAnswerException("Invalid answer format. Expected digit(s) only", e);
      }
    }

}
