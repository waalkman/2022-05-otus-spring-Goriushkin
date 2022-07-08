package com.study.spring.quiz.exam;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.study.spring.quiz.TestUtils;
import com.study.spring.quiz.dto.Question;
import com.study.spring.quiz.exceptions.IncorrectAnswerException;
import org.junit.jupiter.api.Test;

class AnswerFormatValidatorImplTest {

  private static final AnswerFormatValidator ANSWER_FORMAT_VALIDATOR = new AnswerFormatValidatorImpl();

  @Test
  void validateAnswerFormat_correctFreetextAnswer_success() {
    Question question = TestUtils.getFreetextQuestion("correctAnswer");
    assertDoesNotThrow(() -> ANSWER_FORMAT_VALIDATOR.validateAnswerFormat(question, "correctAnswer"));
  }

  @Test
  void validateAnswerFormat_correctOptionsAnswer_success() {
    Question question = TestUtils.getOptionsQuestion();
    assertDoesNotThrow(() -> ANSWER_FORMAT_VALIDATOR.validateAnswerFormat(question, "1 2"));
  }

  @Test
  void validateAnswerFormat_noUserInput_success() {
    Question question = TestUtils.getFreetextQuestion("correctAnswer");
    assertThrows(IncorrectAnswerException.class, () -> ANSWER_FORMAT_VALIDATOR.validateAnswerFormat(question, ""));
  }

  @Test
  void validateAnswerFormat_outOfRangeIndex_success() {
    Question question = TestUtils.getOptionsQuestion();
    assertThrows(
        IncorrectAnswerException.class,
        () -> ANSWER_FORMAT_VALIDATOR.validateAnswerFormat(question, "666 555"),
        "Invalid answer format. Entered index out of range");
  }

  @Test
  void validateAnswerFormat_notANumberAnswer_success() {
    Question question = TestUtils.getOptionsQuestion();
    assertThrows(
        IncorrectAnswerException.class,
        () -> ANSWER_FORMAT_VALIDATOR.validateAnswerFormat(question, "hello there"),
        "Invalid answer format. Expected digit(s) only");
  }
}
