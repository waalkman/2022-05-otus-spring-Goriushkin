package com.study.spring.library.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
public class Comment {

  private String id;
  private String text;
  @Field(name = "user_name")
  private String userName;

  public Comment(String text, String userName) {
    this.id = new ObjectId().toString();
    this.text = text;
    this.userName = userName;
  }

  public Comment copy() {
    Comment comment = new Comment();
    comment.setId(this.id);
    comment.setText(this.text);
    comment.setUserName(this.userName);
    return new Comment();
  }
}
