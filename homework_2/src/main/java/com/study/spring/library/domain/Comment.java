package com.study.spring.library.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

  private String id = new ObjectId().toString();
  private String text;
  @Field(value = "user_name")
  private String userName;

  public Comment copy() {
    return Comment.builder()
                  .id(this.id)
                  .userName(this.userName)
                  .text(this.text)
                  .build();
  }

}
