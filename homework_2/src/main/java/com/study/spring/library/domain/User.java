package com.study.spring.library.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("users")
@AllArgsConstructor
@NoArgsConstructor
public class User {

  private String userName;
  private String pwd;

}