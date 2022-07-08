package com.study.spring.library.exceptions;

public class EntityNotFoundException extends LibraryAppException {

  public EntityNotFoundException(String message) {
    super(message);
  }

  public EntityNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
