package com.study.spring.quiz;

import com.study.spring.quiz.config.ExamParams;
import com.study.spring.quiz.config.ResourceConfig;
import com.study.spring.quiz.dto.Question;
import com.study.spring.quiz.exam.Examiner;
import com.study.spring.quiz.load.QuestionLoader;
import com.study.spring.quiz.load.QuestionsParser;
import com.study.spring.quiz.validate.QuestionValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties({ExamParams.class, ResourceConfig.class})
public class AppRunnerImpl implements AppRunner {

  private final QuestionLoader questionLoader;
  private final QuestionsParser questionsParser;
  private final QuestionValidator questionValidator;
  private final Examiner examiner;

  @Override
  public void run() {
    List<Question> questions = loadQuestions();
    questions.forEach(questionValidator::validateQuestion);
    examiner.examStudent(questions);
  }

  private List<Question> loadQuestions() {
    List<List<String>> rawQuestions = questionLoader.readRawQuestions();
    return questionsParser.parseRawQuestions(rawQuestions);
  }

}
