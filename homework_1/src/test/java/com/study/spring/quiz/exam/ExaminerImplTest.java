package com.study.spring.quiz.exam;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.study.spring.quiz.TestUtils;
import com.study.spring.quiz.config.ExamParams;
import com.study.spring.quiz.dto.Question;
import com.study.spring.quiz.dto.QuestionResult;
import com.study.spring.quiz.exceptions.IncorrectAnswerException;
import com.study.spring.quiz.io.LineReader;
import com.study.spring.quiz.io.LineWriter;
import com.study.spring.quiz.print.QuestionPrinter;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.MessageSource;

@SpringBootTest
class ExaminerImplTest {

  @MockBean
  private QuestionPrinter questionPrinter;
  @SpyBean
  private AnswerChecker answerChecker;
  @SpyBean
  private AnswerFormatValidator answerFormatValidator;
  @MockBean
  private MessageSource messageSource;
  @MockBean
  private LineReader lineReader;
  @MockBean
  private LineWriter lineWriter;
  @MockBean
  private UserNameHolder userNameHolder;
  @MockBean
  private ExamParams examParams;

  @Autowired
  private ExaminerImpl examiner;

  @BeforeEach
  public void initExaminer() {
    when(examParams.getInputRetryThreshold()).thenReturn(1);
    when(examParams.getPassThreshold()).thenReturn(1);
  }

  @Test
  void examStudent_optionsQuestion_success() {
    Question question = TestUtils.getOptionsQuestion();
    String correctAnswer = "1 2";
    when(lineReader.readLine()).thenReturn(correctAnswer);
    when(answerChecker.checkAnswer(question, correctAnswer)).thenReturn(new QuestionResult(question, true));

    examiner.examStudent(List.of(question));

    verify(answerChecker).checkAnswer(question, correctAnswer);
    verify(answerFormatValidator).validateAnswerFormat(question, correctAnswer);
    verify(lineWriter, Mockito.times(2)).writeLine(any());
  }

  @Test
  void examStudent_freetextQuestion_success() {
    String correctAnswer = "Hello spring boot integration test";
    Question question = TestUtils.getFreetextQuestion(correctAnswer);
    when(lineReader.readLine()).thenReturn(correctAnswer);
    when(answerChecker.checkAnswer(question, correctAnswer)).thenReturn(new QuestionResult(question, true));

    examiner.examStudent(List.of(question));

    verify(answerChecker).checkAnswer(question, correctAnswer);
    verify(answerFormatValidator).validateAnswerFormat(question, correctAnswer);
    verify(lineWriter, Mockito.times(2)).writeLine(any());
  }

  @Test
  void examStudent_wrongInputFormat_error() {
    Question question = TestUtils.getOptionsQuestion();
    String userInput = "hello";
    when(lineReader.readLine()).thenReturn(userInput);

    assertThrows(IncorrectAnswerException.class, () -> examiner.examStudent(List.of(question)));

    verify(answerChecker, Mockito.never()).checkAnswer(any(), any());
    verify(answerFormatValidator).validateAnswerFormat(question, userInput);
    verify(lineWriter, Mockito.times(2)).writeLine(any());
  }
}
