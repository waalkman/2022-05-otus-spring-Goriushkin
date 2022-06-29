package com.study.spring.quiz.load;

import static org.mockito.Mockito.when;

import com.study.spring.quiz.dto.Option;
import com.study.spring.quiz.dto.Question;
import com.study.spring.quiz.exceptions.LoadQuestionsException;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;

@SpringBootTest
public class QuestionLoaderTest {

  @MockBean
  private ResourceProvider resourceProvider;
  @Autowired
  private QuestionLoader questionLoader;
  @Autowired
  private QuestionsParser questionsParser;

  @Test
  public void correct_questionsLoad_test() {
    when(resourceProvider.getResource()).thenReturn(new ClassPathResource("questions.csv"));
    Collection<Question> actualQuestions = questionsParser.parseRawQuestions(questionLoader.readRawQuestions());
    Assertions.assertEquals(expectedCorrectQuestions(), actualQuestions);
  }

  private static Collection<Question> expectedCorrectQuestions() {
    return List.of(
        new Question(
            "question_1",
            List.of(new Option("answer_1.1", false), new Option("answer_1.2", true)),
            false),
        new Question(
            "question_2",
            List.of(new Option("***", false)),
            true),
        new Question(
            "question_3",
            List.of(
                new Option("answer_3.1", true),
                new Option("answer_3.2", false),
                new Option("answer_3.3", true)),
            false));
  }

  @Test
  public void incorrect_questionsLoad_test() {
    when(resourceProvider.getResource()).thenReturn(new ClassPathResource("no_answer_questions.csv"));
    Assertions.assertThrows(LoadQuestionsException.class, () -> questionLoader.readRawQuestions());
  }

}
