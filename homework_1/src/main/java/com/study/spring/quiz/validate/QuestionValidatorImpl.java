package com.study.spring.quiz.validate;

import com.study.spring.quiz.Constants;
import com.study.spring.quiz.dto.Option;
import com.study.spring.quiz.dto.Question;
import com.study.spring.quiz.exceptions.ValidateQuestionException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class QuestionValidatorImpl implements QuestionValidator {

  public void validateQuestion(Question question) {
    validateTitle(question.getTitle());
    question.getOptions().forEach(option -> validateOptions(question.getTitle(), question.getOptions()));
  }

  private void validateTitle(String title) {
    if (!StringUtils.hasLength(title)) {
      throw new ValidateQuestionException("Empty question title");
    }
  }

  private void validateOptions(String title, List<Option> options) {
    if (!isValidOptions(options)) {
      throw new ValidateQuestionException(String.format("Invalid answers for question: '%s'", title));
    } else {
      LOG.debug("Question '{}' valid", title);
    }
  }

  private boolean isValidOptions(List<Option> options) {
    return isValidFreetextAnswer(options) || isValidChooseAnswer(options);
  }

  private boolean isValidFreetextAnswer(List<Option> options) {
    return options.size() == 1
        && Constants.FREE_TEXT_ANSWER_MARK.equals(options.get(0).getText())
        && options.get(0).getText() != null
        && !options.get(0).getText().isEmpty();
  }

  private boolean isValidChooseAnswer(List<Option> options) {
    return options.stream().anyMatch(Option::isCorrect);
  }
}
