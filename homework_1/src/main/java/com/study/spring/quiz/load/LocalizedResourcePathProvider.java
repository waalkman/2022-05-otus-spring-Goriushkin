package com.study.spring.quiz.load;

import java.io.File;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class LocalizedResourcePathProvider implements ResourcePathProvider {

  private final LanguageProviderImpl languageProvider;
  @Value("${questions.fileName}")
  private String resourceFileName;

  private String constructLocalizedResourcePathCandidate() {
    return languageProvider.getLanguage() + File.separator + resourceFileName;
  }

  @Override
  public String getLocalizedResourcePath() {
    String localizedResourcePathCandidate = constructLocalizedResourcePathCandidate();
    if (StringUtils.hasLength(languageProvider.getLanguage()) && resourceExists(localizedResourcePathCandidate)) {
      return localizedResourcePathCandidate;
    } else {
      return "default" + File.separator + resourceFileName;
    }
  }

  private boolean resourceExists(String resourcePath) {
    return new ClassPathResource(resourcePath).isReadable();
  }

}
