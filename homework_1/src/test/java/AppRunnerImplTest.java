import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;

import com.study.spring.quiz.AppRunnerImpl;
import com.study.spring.quiz.exam.Examiner;
import com.study.spring.quiz.load.QuestionLoader;
import com.study.spring.quiz.load.QuestionsParser;
import com.study.spring.quiz.validate.QuestionValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AppRunnerImplTest {

  @Spy
  private QuestionLoader questionLoader;
  @Spy
  private QuestionsParser questionsParser;
  @Spy
  private QuestionValidator questionValidator;
  @Spy
  private Examiner examiner;
  @InjectMocks
  private AppRunnerImpl appRunner;

  @Test
  void run_normalRun_success() {
    appRunner.run();
    verify(questionLoader).readRawQuestions();
    verify(questionsParser).parseRawQuestions(anyList());
    verify(examiner).examStudent(anyList());
  }
}
