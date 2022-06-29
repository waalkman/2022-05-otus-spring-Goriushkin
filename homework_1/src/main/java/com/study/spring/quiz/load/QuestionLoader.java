package com.study.spring.quiz.load;

import java.util.List;

public interface QuestionLoader {

  List<List<String>> readRawQuestions();

}
