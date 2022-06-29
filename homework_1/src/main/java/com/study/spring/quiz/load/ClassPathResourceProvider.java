package com.study.spring.quiz.load;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClassPathResourceProvider implements ResourceProvider {

  private final ResourcePathProvider resourcePathProvider;

  @Override
  public Resource getResource() {
    return new ClassPathResource(resourcePathProvider.getLocalizedResourcePath());
  }

}
