package com.study.spring.library.exceptions;

public class LibraryAppException extends RuntimeException {

  public LibraryAppException(String message) {
    super(message);
  }

  public LibraryAppException(String message, Throwable cause) {
    super(message, cause);
  }

}
