package com.study.spring.quiz.load;

import com.study.spring.quiz.Constants;
import com.study.spring.quiz.Utils;
import com.study.spring.quiz.dto.Option;
import com.study.spring.quiz.dto.Question;
import com.study.spring.quiz.exceptions.LoadQuestionsException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.stereotype.Component;

@Component
public class QuestionsParserImpl implements QuestionsParser {

  @Override
  public List<Question> parseRawQuestions(List<List<String>> rawQuestions) {
    return rawQuestions.stream()
                       .map(this::parseQuestion)
                       .collect(Collectors.toList());
  }

  private Question parseQuestion(List<String> rawQuestion) {
    try {
      Iterator<String> questionIterator = rawQuestion.iterator();
      String title = questionIterator.next();
      List<String> answers = Arrays.asList(questionIterator.next().split(",", -1));
      List<String> correctMarks = Arrays.asList(questionIterator.next().split(",", -1));
      boolean isFreetext = Utils.isFreeTextAnswer(answers);
      return new Question(title, constructOptions(isFreetext, answers, correctMarks), isFreetext);
    } catch (NoSuchElementException e) {
      throw new LoadQuestionsException("Incorrect questions file line format", e);
    }
  }

  private List<Option> constructOptions(boolean isFreetext, List<String> answers, List<String> correctMarks) {
    if (isFreetext) {
      return List.of(new Option(correctMarks.get(0), true));
    } else {
      return IntStream.range(0, answers.size())
                      .boxed()
                      .map(i -> new Option(answers.get(i), Constants.CORRECT_ANSWER_MARK.equals(correctMarks.get(i))))
                      .collect(Collectors.toList());
    }
  }
}
