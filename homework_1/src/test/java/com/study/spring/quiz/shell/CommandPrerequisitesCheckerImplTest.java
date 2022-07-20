package com.study.spring.quiz.shell;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.study.spring.quiz.exam.UserNameHolder;
import com.study.spring.quiz.io.LineWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

@ExtendWith(MockitoExtension.class)
class CommandPrerequisitesCheckerImplTest {

  @Mock
  private UserNameHolder userNameHolder;
  @Spy
  private LineWriter lineWriter;
  @Mock
  private MessageSource messageSource;

  @InjectMocks
  private CommandPrerequisitesCheckerImpl commandPrerequisitesChecker;

  @Test
  void canRunTests_userGaveName_success() {
    when(userNameHolder.getName()).thenReturn("name");
    assertTrue(commandPrerequisitesChecker.canRunTests());
  }

  @Test
  void canRunTests_noNameEntered_success() {
    assertFalse(commandPrerequisitesChecker.canRunTests());
    verify(lineWriter).writeLine(any());
    verify(messageSource).getMessage(any(), any(), any());
  }
}
