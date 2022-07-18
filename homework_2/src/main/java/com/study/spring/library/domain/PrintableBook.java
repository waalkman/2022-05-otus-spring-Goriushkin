package com.study.spring.library.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PrintableBook {

  private final Long id;
  private final String title;
  private final String description;
  private final String genre;
  private final String author;

}
