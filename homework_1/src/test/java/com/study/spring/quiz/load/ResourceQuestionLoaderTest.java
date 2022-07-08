package com.study.spring.quiz.load;

import static org.mockito.Mockito.when;

import com.study.spring.quiz.exceptions.LoadQuestionsException;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;

@SpringBootTest
public class ResourceQuestionLoaderTest {

  @MockBean
  private ResourceProvider resourceProvider;
  @Autowired
  private QuestionLoader questionLoader;

  private static List<List<String>> expectedCorrectQuestions() {
    return List.of(
        List.of("question_1", "answer_1.1,answer_1.2", ",+"),
        List.of("question_2", "***", "answer2"),
        List.of("question_3", "answer_3.1,answer_3.2,answer_3.3", "+,,+"));
  }

  @Test
  void correct_readRawQuestions_test() {
    when(resourceProvider.getResource()).thenReturn(new ClassPathResource("questions.csv"));
    List<List<String>> actualRawQuestions = questionLoader.readRawQuestions();
    Assertions.assertEquals(expectedCorrectQuestions(), actualRawQuestions);
  }

  @Test
  void incorrect_questionsLoad_test() {
    when(resourceProvider.getResource()).thenReturn(new ClassPathResource("no_answer_questions.csv"));
    Assertions.assertThrows(
        LoadQuestionsException.class,
        () -> questionLoader.readRawQuestions(),
        "Incorrect questions file format");
  }

  @Test
  void streamError_questionsLoad_test() {
    when(resourceProvider.getResource()).thenReturn(new ClassPathResource("not_existing_resource"));
    Assertions.assertThrows(
        LoadQuestionsException.class,
        () -> questionLoader.readRawQuestions(),
        "Error reading questions!");
  }

}
