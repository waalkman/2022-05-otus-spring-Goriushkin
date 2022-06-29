package com.study.spring.quiz.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Question {

  private final String title;
  private final List<Option> options;
  private final boolean freeTextAnswer;

}
