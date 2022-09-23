package com.study.spring.batch.mongo.model;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("authors")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Author {

  @Id
  private String id;
  @NotEmpty
  private String name;

}
