package com.study.spring.quiz.config;

import javax.validation.constraints.Positive;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("test")
@Validated
@Data
public class ExamParams {

  @Positive
  private Integer passThreshold;
  @Positive
  private Integer inputRetryThreshold;

}
