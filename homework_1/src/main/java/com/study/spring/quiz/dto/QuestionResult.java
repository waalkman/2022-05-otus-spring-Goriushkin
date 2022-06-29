package com.study.spring.quiz.dto;

import lombok.Data;

@Data
public class QuestionResult {

  private final Question question;
  private final boolean answerIsCorrect;

}
