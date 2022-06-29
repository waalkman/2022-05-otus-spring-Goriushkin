package com.study.spring.quiz.exam;

import com.study.spring.quiz.dto.Option;
import com.study.spring.quiz.dto.Question;
import com.study.spring.quiz.dto.QuestionResult;
import org.springframework.stereotype.Component;

@Component
public class AnswerCheckerImpl implements AnswerChecker {

  @Override
  public QuestionResult checkAnswer(Question question, String userAnswer) {
    if (question.isFreeTextAnswer()) {
      return new QuestionResult(question, isFreetextAnswerCorrect(question, userAnswer));
    } else {
      return new QuestionResult(question, isOptionsAnswerCorrect(question, userAnswer));
    }
  }

  private boolean isFreetextAnswerCorrect(Question question, String userAnswer) {
    return question.getOptions().get(0).getText().equalsIgnoreCase(userAnswer);
  }

  private boolean isOptionsAnswerCorrect(Question question, String userAnswer) {
    String[] answers = userAnswer.split(" ", -1);
    return enteredCorrectAmountOfAnswers(question, answers) && allEnteredAnswersIsCorrect(question, answers);
  }

  private boolean enteredCorrectAmountOfAnswers(Question question, String[] answers) {
    long correctOptionsAmount = question.getOptions()
                                        .stream()
                                        .filter(Option::isCorrect)
                                        .count();

    return answers.length == correctOptionsAmount;
  }

  private boolean allEnteredAnswersIsCorrect(Question question, String[] answers) {
    for (String answer : answers) {
      int answerNumber = Integer.parseInt(answer) - 1;
      if (!question.getOptions().get(answerNumber).isCorrect()) {
        return false;
      }
    }
    return true;
  }

}
