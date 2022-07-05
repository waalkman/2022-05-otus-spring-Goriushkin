package com.study.spring.quiz.load;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class LanguageProviderImplTest {

  private LanguageProviderImpl languageProvider;

  @BeforeEach
  public void init() {
    System.setProperty("user.language", "");
    System.setProperty("user.country", "");
  }

  @ParameterizedTest
  @ValueSource(strings = {"en", "ru", "fr"})
  void getLanguage_fromLangProp_success(String input) {
    System.setProperty("user.language", input);
    languageProvider = new LanguageProviderImpl();
    assertEquals(input, languageProvider.getLanguage());
  }

  @ParameterizedTest
  @ValueSource(strings = {"EN", "RU", "FR"})
  void getLanguage_fromCountryProp_success(String input) {
    System.setProperty("user.country", input);
    languageProvider = new LanguageProviderImpl();
    assertEquals(input.toLowerCase(), languageProvider.getLanguage());
  }

}
