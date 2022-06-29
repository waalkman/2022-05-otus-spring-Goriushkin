package com.study.spring.quiz.load;

import com.study.spring.quiz.exceptions.LoadQuestionsException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResourceQuestionLoader implements QuestionLoader {

  private final ResourceProvider resourceProvider;

  @Override
  public List<List<String>> readRawQuestions() {
    List<List<String>> rawQuestions = new ArrayList<>();
    try (Scanner scanner = new Scanner(resourceProvider.getResource().getInputStream())) {
      LOG.debug("Reading questions file");
      while (scanner.hasNextLine()) {
        LOG.debug("Reading question");
        rawQuestions.add(readRawQuestion(scanner));
      }
      LOG.debug("Read all questions");
    } catch (IOException e) {
      throw new LoadQuestionsException("Error reading questions!", e);
    }

    return rawQuestions;
  }

  private List<String> readRawQuestion(Scanner scanner) {
    try {
      String title = scanner.nextLine();
      LOG.trace("Read raw title: {}", title);
      String answers = scanner.nextLine();
      LOG.trace("Read raw answers: {}", answers);
      String correctMarks = scanner.nextLine();
      LOG.trace("Read raw correctMarks: {}", correctMarks);
      return List.of(title, answers, correctMarks);
    } catch (NoSuchElementException e) {
      throw new LoadQuestionsException("Incorrect questions file format", e);
    }
  }
}
