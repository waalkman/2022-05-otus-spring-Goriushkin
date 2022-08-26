package com.study.spring.library.dto;

import com.study.spring.library.domain.Book;
import com.study.spring.library.domain.Comment;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BookDto {

  private String id;
  private String title;
  private String description;
  private String genre;
  private String author;
  private List<Comment> comments;

  public Book toBook() {
    return Book.builder()
               .id(this.id)
               .title(this.title)
               .description(this.description)
               .build();
  }

}
