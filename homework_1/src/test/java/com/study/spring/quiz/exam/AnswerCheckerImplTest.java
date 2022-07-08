package com.study.spring.quiz.exam;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.study.spring.quiz.TestUtils;
import com.study.spring.quiz.dto.Question;
import com.study.spring.quiz.dto.QuestionResult;
import org.junit.jupiter.api.Test;

class AnswerCheckerImplTest {

  private static final String CORRECT_FREETEXT_ANSWER = "freetext_answer";
  private static final AnswerChecker ANSWER_CHECKER = new AnswerCheckerImpl();

  @Test
  void checkAnswer_freetextAnswer_correct() {
    Question question = TestUtils.getFreetextQuestion(CORRECT_FREETEXT_ANSWER);
    QuestionResult questionResult = ANSWER_CHECKER.checkAnswer(question, CORRECT_FREETEXT_ANSWER);
    assertEquals(question, questionResult.getQuestion());
    assertTrue(questionResult.isAnswerIsCorrect());
  }

  @Test
  void checkAnswer_freetextAnswer_wrong() {
    Question question = TestUtils.getFreetextQuestion(CORRECT_FREETEXT_ANSWER);
    QuestionResult questionResult = ANSWER_CHECKER.checkAnswer(question, "wrong answer");
    assertEquals(question, questionResult.getQuestion());
    assertFalse(questionResult.isAnswerIsCorrect());
  }

  @Test
  void checkAnswer_optionsAnswer_correct() {
    Question question = TestUtils.getOptionsQuestion();
    QuestionResult questionResult = ANSWER_CHECKER.checkAnswer(question, "1 2");
    assertEquals(question, questionResult.getQuestion());
    assertTrue(questionResult.isAnswerIsCorrect());
  }

  @Test
  void checkAnswer_optionsAnswer_wrong() {
    Question question = TestUtils.getOptionsQuestion();
    QuestionResult questionResult = ANSWER_CHECKER.checkAnswer(question, "1 3");
    assertEquals(question, questionResult.getQuestion());
    assertFalse(questionResult.isAnswerIsCorrect());
  }

}
