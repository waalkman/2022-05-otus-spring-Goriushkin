package com.study.spring.batch.postgre.exceptions;

public class ConsistencyException extends RuntimeException {

  public ConsistencyException(String message) {
    super(message);
  }
}
