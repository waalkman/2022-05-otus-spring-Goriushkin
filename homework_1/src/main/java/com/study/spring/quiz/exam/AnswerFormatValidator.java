package com.study.spring.quiz.exam;

import com.study.spring.quiz.dto.Question;
import com.study.spring.quiz.exceptions.IncorrectAnswerException;
import org.springframework.stereotype.Component;

@Component
public class AnswerFormatValidator {

  public void validateAnswerFormat(Question question, String userAnswer) {
    if (question.isFreeTextAnswer()) {
      validateFreetextAnswer(userAnswer);
    } else {
      validateOptionsAnswer(userAnswer);
    }
  }

  private void validateFreetextAnswer(String userAnswer) {
    if (userAnswer == null || userAnswer.isEmpty()) {
      throw new IncorrectAnswerException("Invalid freetext answer format");
    }
  }

  private void validateOptionsAnswer(String userAnswer) {
      String[] answers = userAnswer.split(" ", -1);
      try {
        for (String answer : answers) {
          Integer.parseInt(answer);
        }
      } catch (NumberFormatException e) {
        throw new IncorrectAnswerException("Invalid answer format. Expected digit(s) only", e);
      }
    }

}
