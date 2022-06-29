package com.study.spring.quiz.exam;

import com.study.spring.quiz.dto.Question;
import com.study.spring.quiz.dto.QuestionResult;

public interface AnswerChecker {

  QuestionResult checkAnswer(Question question, String userAnswer);

}
