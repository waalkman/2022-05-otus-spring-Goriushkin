package com.study.spring.quiz;

import com.study.spring.quiz.dto.Option;
import com.study.spring.quiz.dto.Question;
import java.util.List;

public class TestUtils {

  public static Question getFreetextQuestion(String answer) {
    return new Question("title", List.of(new Option(answer, true)), true);
  }

  public static Question getOptionsQuestion() {
    return new Question(
        "title",
        List.of(
            new Option("option_1", true),
            new Option("option_2", true),
            new Option("option_3", false)),
        false);
  }

}
