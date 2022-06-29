package com.study.spring.quiz;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {

  public static boolean isFreeTextAnswer(List<String> answers) {
    return Constants.FREE_TEXT_ANSWER_MARK.equals(answers.iterator().next());
  }

}
