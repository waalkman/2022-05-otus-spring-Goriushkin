package com.study.spring.library.exceptions;

public class DataQueryException extends LibraryAppException {

  public DataQueryException(String message) {
    super(message);
  }

  public DataQueryException(String message, Throwable cause) {
    super(message, cause);
  }

}
