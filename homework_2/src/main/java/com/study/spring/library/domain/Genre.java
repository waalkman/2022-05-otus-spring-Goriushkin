package com.study.spring.library.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("genres")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Genre {

  @Id
  private String id;
  private String name;

}
