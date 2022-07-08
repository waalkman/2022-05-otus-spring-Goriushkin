package com.study.spring.quiz.io;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class ResourceExistenceCheckerImpl implements ResourceExistenceChecker {

  @Override
  public boolean resourceExists(String path) {
    return new ClassPathResource(path).isReadable();
  }
}
