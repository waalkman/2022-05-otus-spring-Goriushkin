package com.study.spring.library;

import com.study.spring.library.config.InputConfig;
import io.mongock.runner.springboot.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableMongock
@EnableConfigurationProperties({InputConfig.class})
public class Main {

  public static void main(String[] args) {
    SpringApplication.run(Main.class);
  }
}
