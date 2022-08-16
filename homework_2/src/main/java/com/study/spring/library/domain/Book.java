package com.study.spring.library.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("books")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book {

  @Id
  private String id;
  private String title;
  private String description;
  @DBRef
  private Genre genre;
  @DBRef
  private Author author;
  private List<Comment> comments;

}
