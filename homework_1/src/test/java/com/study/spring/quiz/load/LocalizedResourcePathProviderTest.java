package com.study.spring.quiz.load;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.study.spring.quiz.config.ResourceConfig;
import com.study.spring.quiz.io.ResourceExistenceChecker;
import java.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LocalizedResourcePathProviderTest {

  private static final String RESOURCE_FILE_NAME = "name";
  private static final String DEFAULT_RESOURCE_PATH = "default" + File.separator + RESOURCE_FILE_NAME;
  @Mock
  private LanguageProvider languageProvider;
  @Mock
  private ResourceConfig resourceConfig;
  @Mock
  private ResourceExistenceChecker resourceExistenceChecker;
  @InjectMocks
  private LocalizedResourcePathProvider localizedResourcePathProvider;

  @BeforeEach
  public void init() {
    when(resourceConfig.getFileName()).thenReturn(RESOURCE_FILE_NAME);
  }

  @Test
  void getLocalizedResourcePath_withoutLang_success() {
    String actualResourcePath = localizedResourcePathProvider.getLocalizedResourcePath();
    assertEquals(DEFAULT_RESOURCE_PATH, actualResourcePath);
  }

  @Test
  void getLocalizedResourcePath_withLang_success() {
    String locale = "fr";
    String expectedResourcePath = buildLocalizedResourcePath(locale);
    when(languageProvider.getLanguage()).thenReturn(locale);
    when(resourceExistenceChecker.resourceExists(any())).thenReturn(true);

    String actualResourcePath = localizedResourcePathProvider.getLocalizedResourcePath();

    assertEquals(expectedResourcePath, actualResourcePath);
  }

  private String buildLocalizedResourcePath(String locale) {
    return locale + File.separator + resourceConfig.getFileName();
  }
}
