package com.study.spring.quiz.load;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.study.spring.quiz.Constants;
import com.study.spring.quiz.dto.Option;
import com.study.spring.quiz.dto.Question;
import com.study.spring.quiz.exceptions.LoadQuestionsException;
import java.util.List;
import org.junit.jupiter.api.Test;

class QuestionsParserImplTest {

  private static final String TITLE = "title";
  private static final String OPTION_1 = "answer_1";
  private static final String OPTION_2 = "answer_2";
  private static final String FREETEXT_ANSWER = "answer_free";
  private final QuestionsParser questionsParser = new QuestionsParserImpl();

  @Test
  void parseRawQuestions_optionsQuestion_success() {
    List<String> rawQuestions = List.of(TITLE, OPTION_1 + ',' + OPTION_2, "+,");
    List<Question> expectedQuestions =
        List.of(
            new Question(
                TITLE,
                List.of(
                    new Option(OPTION_1, true),
                    new Option(OPTION_2, false)),
                false));


    List<Question> actualQuestions = questionsParser.parseRawQuestions(List.of(rawQuestions));

    assertEquals(expectedQuestions, actualQuestions);
  }

  @Test
  void parseRawQuestions_freetextQuestion_success() {
    List<String> rawQuestions = List.of(TITLE, Constants.FREE_TEXT_ANSWER_MARK, FREETEXT_ANSWER);
    List<Question> expectedQuestions =
        List.of(new Question(TITLE, List.of(new Option(FREETEXT_ANSWER, true)), true));

    List<Question> actualQuestions = questionsParser.parseRawQuestions(List.of(rawQuestions));

    assertEquals(expectedQuestions, actualQuestions);
  }

  @Test
  void parseRawQuestions_invalidFileContent_error() {
    List<String> rawQuestions = List.of("", "");
    assertThrows(LoadQuestionsException.class, () -> questionsParser.parseRawQuestions(List.of(rawQuestions)));
  }
}
