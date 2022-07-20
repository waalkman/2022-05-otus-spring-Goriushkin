package com.study.spring.quiz.shell;

import com.study.spring.quiz.exam.UserNameHolder;
import com.study.spring.quiz.io.LineWriter;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class CommandPrerequisitesCheckerImpl implements CommandPrerequisitesChecker {

  private final UserNameHolder userNameHolder;
  private final LineWriter lineWriter;
  private final MessageSource messageSource;

  @Override
  public boolean canRunTests() {
    if (!StringUtils.hasLength(userNameHolder.getName())) {
      lineWriter.writeLine(messageSource.getMessage("enter.name", null, Locale.getDefault()));
      return false;
    } else {
      return true;
    }
  }

}
