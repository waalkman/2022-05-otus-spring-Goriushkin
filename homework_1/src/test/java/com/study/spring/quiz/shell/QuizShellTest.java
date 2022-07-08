package com.study.spring.quiz.shell;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.study.spring.quiz.AppRunner;
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
class QuizShellTest {

  @Spy
  private AppRunner appRunner = mock(AppRunner.class);
  @Mock
  private UserNameHolder userNameHolder;
  @Mock
  private LineWriter lineWriter;
  @Mock
  private MessageSource messageSource;
  @Mock
  private CommandPrerequisitesChecker prerequisitesChecker;
  @InjectMocks
  private QuizShell quizShell;

  @Test
  void runTests_canRun_testRun() {
    when(prerequisitesChecker.canRunTests()).thenReturn(true);
    quizShell.runTests();
    verify(appRunner).run();
  }

  @Test
  void runTests_canNotRun_testNotRun() {
    when(prerequisitesChecker.canRunTests()).thenReturn(false);
    quizShell.runTests();
    verify(appRunner, never()).run();
  }

  @Test
  void askName_canRun_testRun() {
    String expectedName = "expectedName";
    quizShell.askName(expectedName);
    verify(userNameHolder).setName(expectedName);
    verify(lineWriter).writeLine(any());
  }
}
