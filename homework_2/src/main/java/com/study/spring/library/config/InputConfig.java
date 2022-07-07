package com.study.spring.library.config;

import javax.validation.constraints.Positive;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("input")
@Data
@Validated
public class InputConfig {

  @Positive
  private Integer retriesThreshold;

}
