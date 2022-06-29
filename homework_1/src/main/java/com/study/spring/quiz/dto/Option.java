package com.study.spring.quiz.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Option {

  private final String text;
  private final boolean isCorrect;

}
