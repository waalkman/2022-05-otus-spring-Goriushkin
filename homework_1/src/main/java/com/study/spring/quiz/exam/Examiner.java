package com.study.spring.quiz.exam;

import com.study.spring.quiz.dto.Question;
import java.util.List;

public interface Examiner {

  void examStudent(List<Question> questions);

}
