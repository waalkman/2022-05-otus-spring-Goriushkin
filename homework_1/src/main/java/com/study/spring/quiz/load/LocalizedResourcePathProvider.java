package com.study.spring.quiz.load;

import com.study.spring.quiz.config.ResourceConfig;
import com.study.spring.quiz.io.ResourceExistenceChecker;
import java.io.File;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class LocalizedResourcePathProvider implements ResourcePathProvider {

  private final LanguageProvider languageProvider;
  private final ResourceConfig resourceConfig;
  private final ResourceExistenceChecker resourceExistenceChecker;

  @Override
  public String getLocalizedResourcePath() {
    String localizedResourcePathCandidate = constructLocalizedResourcePathCandidate();
    if (StringUtils.hasLength(languageProvider.getLanguage()) && resourceExists(localizedResourcePathCandidate)) {
      return localizedResourcePathCandidate;
    } else {
      return "default" + File.separator + resourceConfig.getFileName();
    }
  }

  private String constructLocalizedResourcePathCandidate() {
    return languageProvider.getLanguage() + File.separator + resourceConfig.getFileName();
  }

  private boolean resourceExists(String resourcePath) {
    return resourceExistenceChecker.resourceExists(resourcePath);
  }

}
