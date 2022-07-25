package com.study.spring.library.exceptions;

import lombok.Getter;

public class EntityNotFoundException extends LibraryAppException {

  @Getter
  private final String entity;

  public EntityNotFoundException(String message, String entity) {
    super(message);
    this.entity = entity;
  }

  public EntityNotFoundException(String message, Throwable cause, String entity) {
    super(message, cause);
    this.entity = entity;
  }

}
