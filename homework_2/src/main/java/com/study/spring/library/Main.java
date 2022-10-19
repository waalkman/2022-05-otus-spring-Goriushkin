package com.study.spring.library;

import io.mongock.runner.springboot.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableMongock
@EnableCircuitBreaker
@EnableEurekaClient
public class Main {

  public static void main(String[] args) {
    SpringApplication.run(Main.class);
  }
}
