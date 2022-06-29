package com.study.spring.quiz;

import com.study.spring.quiz.dto.Question;
import com.study.spring.quiz.exam.Examiner;
import com.study.spring.quiz.load.QuestionLoader;
import com.study.spring.quiz.load.QuestionsParser;
import com.study.spring.quiz.load.ResourceQuestionLoader;
import com.study.spring.quiz.validate.QuestionValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;

@RequiredArgsConstructor
public class AppRunner {

  private final ApplicationContext context;

  public void run() {
    List<Question> questions = loadQuestions();
    validateQuestions(questions);
    examStudent(questions);
  }

  private List<Question> loadQuestions() {
    QuestionLoader questionLoader = context.getBean(ResourceQuestionLoader.class);
    QuestionsParser questionsParser = context.getBean(QuestionsParser.class);
    List<List<String>> rawQuestions = questionLoader.readRawQuestions();
    return questionsParser.parseRawQuestions(rawQuestions);
  }

  private void validateQuestions(List<Question> questions) {
    QuestionValidator questionValidator = context.getBean(QuestionValidator.class);
    questions.forEach(questionValidator::validateQuestion);
  }

  private void examStudent(List<Question> questions) {
    Examiner examiner = context.getBean(Examiner.class);
    examiner.examStudent(questions);
  }

}
