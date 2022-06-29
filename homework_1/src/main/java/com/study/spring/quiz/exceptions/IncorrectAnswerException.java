package com.study.spring.quiz.exceptions;

public class IncorrectAnswerException extends QuizException {

  public IncorrectAnswerException(String message) {
    super(message);
  }

  public IncorrectAnswerException(String message, Throwable cause) {
    super(message, cause);
  }
}
