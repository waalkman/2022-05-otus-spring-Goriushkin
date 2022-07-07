package com.study.spring.library.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Book {

  private final Long id;
  private final String title;
  private final String description;
  private final Long genreId;
  private final Long authorId;

}
