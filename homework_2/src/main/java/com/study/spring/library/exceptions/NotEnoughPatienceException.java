package com.study.spring.library.exceptions;

public class NotEnoughPatienceException extends LibraryAppException {

  public NotEnoughPatienceException(String message) {
    super(message);
  }

  public NotEnoughPatienceException(String message, Throwable cause) {
    super(message, cause);
  }
}
