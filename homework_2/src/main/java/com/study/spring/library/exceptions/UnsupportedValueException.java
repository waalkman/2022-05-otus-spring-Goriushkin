package com.study.spring.library.exceptions;

public class UnsupportedValueException extends LibraryAppException {

  public UnsupportedValueException(String message) {
    super(message);
  }

  public UnsupportedValueException(String message, Throwable cause) {
    super(message, cause);
  }
}
