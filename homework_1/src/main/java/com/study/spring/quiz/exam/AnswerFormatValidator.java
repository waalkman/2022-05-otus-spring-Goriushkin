package com.study.spring.quiz.exam;

import com.study.spring.quiz.dto.Question;

public interface AnswerFormatValidator {

  void validateAnswerFormat(Question question, String userAnswer);

}
