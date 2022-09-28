package com.study.spring.library.indicators;

import com.study.spring.library.service.BookService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookIndicator implements HealthIndicator {

  private final BookService bookService;

  @Override
  public Health health() {
    if (bookService.getAll().size() <= 1) {
      return Health.outOfService()
                   .withDetails(
                       Map.of(
                           "Heeeey!", "there is one",
                           "or less books", "in database"))
                   .build();
    } else {
      return Health.status(Status.UP)
                   .withDetails(
                       Map.of("Wazzzup?!", "we r ok"))
                   .build();
    }
  }
}
