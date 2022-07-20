package com.study.spring.quiz.config;

import javax.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("questions")
@Validated
@Data
public class ResourceConfig {

  @NotEmpty
  private String fileName;

}
