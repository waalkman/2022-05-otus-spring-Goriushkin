package com.study.spring.quiz.exam;

import org.springframework.stereotype.Component;

@Component
public class UserNameHolderImpl implements UserNameHolder {

  private String name;

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

}
