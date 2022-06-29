package com.study.spring.quiz.exceptions;

public class QuizException extends RuntimeException {

  public QuizException(String message) {
    super(message);
  }

  public QuizException(String message, Throwable cause) {
    super(message, cause);
  }
}
