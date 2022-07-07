package com.study.spring.library.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Genre {

  private final Long id;
  private final String name;

}
