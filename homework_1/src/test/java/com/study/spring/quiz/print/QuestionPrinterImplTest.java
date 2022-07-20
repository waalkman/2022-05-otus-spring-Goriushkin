package com.study.spring.quiz.print;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.study.spring.quiz.TestUtils;
import com.study.spring.quiz.dto.Question;
import com.study.spring.quiz.io.LineWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class QuestionPrinterImplTest {

  @Spy
  private LineWriter lineWriter;
  @InjectMocks
  private QuestionPrinterImpl questionPrinter;

  @Test
  void printQuestion_optionsQuestion_success() {
    Question question = TestUtils.getOptionsQuestion();

    questionPrinter.printQuestion(question);

    verify(lineWriter, times(6)).writeLine(any());
  }

  @Test
  void printQuestion_freetextQuestion_success() {
    Question question = TestUtils.getFreetextQuestion("any");

    questionPrinter.printQuestion(question);

    verify(lineWriter, times(3)).writeLine(any());
  }
}
