package com.study.spring.quiz;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@ComponentScan
@Configuration
@PropertySource("classpath:application.properties")
public class AppStarter {

  public static void main(String[] args) {
    ApplicationContext context = new AnnotationConfigApplicationContext(AppStarter.class);
    new AppRunner(context).run();
  }

}
