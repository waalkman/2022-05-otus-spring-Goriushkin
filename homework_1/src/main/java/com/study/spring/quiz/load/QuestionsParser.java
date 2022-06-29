package com.study.spring.quiz.load;

import com.study.spring.quiz.dto.Question;
import java.util.List;

public interface QuestionsParser {

  List<Question> parseRawQuestions(List<List<String>> rawQuestions);

}
