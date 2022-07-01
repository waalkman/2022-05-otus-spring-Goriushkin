package com.study.spring.quiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class AppStarter {

  public static void main(String[] args) {
    ApplicationContext ctx = SpringApplication.run(AppStarter.class, args);
  }

}
