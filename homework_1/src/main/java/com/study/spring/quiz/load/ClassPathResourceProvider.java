package com.study.spring.quiz.load;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class ClassPathResourceProvider implements ResourceProvider {

  @Value("${questions.file.name}")
  private String questionsFileName;

  @Override
  public Resource getResource() {
    return new ClassPathResource(questionsFileName);
  }

}
