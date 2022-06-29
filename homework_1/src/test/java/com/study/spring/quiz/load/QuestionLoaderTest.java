package com.study.spring.quiz.load;

import static org.mockito.Mockito.when;

import com.study.spring.quiz.dto.Option;
import com.study.spring.quiz.dto.Question;
import com.study.spring.quiz.exceptions.LoadQuestionsException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;

@ExtendWith(MockitoExtension.class)
public class QuestionLoaderTest {

  @Mock
  private Resource resource;
  @InjectMocks
  private ResourceQuestionLoader questionLoader;

  @Test
  public void correct_questionsLoad_test() throws IOException {
    when(resource.getInputStream()).thenReturn(getResourceStream("questions.csv"));

    Collection<Question> actualQuestions = questionLoader.loadQuestions();

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
  public void incorrect_questionsLoad_test() throws IOException {
    when(resource.getInputStream()).thenReturn(getResourceStream("no_answer_questions.csv"));
    Assertions.assertThrows(LoadQuestionsException.class, () -> questionLoader.loadQuestions());
  }

  private InputStream getResourceStream(String resourceName) {
    return getClass().getClassLoader().getResourceAsStream(resourceName);
  }
}
