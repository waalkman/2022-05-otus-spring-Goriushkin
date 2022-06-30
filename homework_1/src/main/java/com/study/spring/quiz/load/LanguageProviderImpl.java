package com.study.spring.quiz.load;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class LanguageProviderImpl implements LanguageProvider {

  private String language;

  public LanguageProviderImpl() {
    String languageProp = System.getProperty("user.language");
    String countryProp = System.getProperty("user.country");

    if (StringUtils.hasLength(languageProp)) {
      this.language = languageProp;
    } else if (StringUtils.hasLength(countryProp)) {
      language = countryProp.toLowerCase();
    }
  }

  @Override
  public String getLanguage() {
    return this.language;
  }
}
