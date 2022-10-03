package com.study.spring.integration;

import com.study.spring.integration.domain.Receipt;
import com.study.spring.integration.generator.PersonGenerator;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;

@IntegrationComponentScan
@ComponentScan
@EnableIntegration
public class Main {

  public static void main(String[] args) throws InterruptedException {
    AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(Main.class);

    PublishSubscribeChannel outputChannel = ctx.getBean("outputChannel", PublishSubscribeChannel.class);
    outputChannel.subscribe(m -> System.out.println("Bank received tax calculation: " + m.getPayload()));

    PersonGenerator personGenerator = ctx.getBean(PersonGenerator.class);
    TaxService taxService = ctx.getBean(TaxService.class);

    ForkJoinPool pool = ForkJoinPool.commonPool();
    while (true) {
      Thread.sleep(1000L);
      pool.execute(
          () -> {
            System.out.println("Before calculating total tax");
            Receipt total = taxService.calculate(personGenerator.generate(new Random().nextInt(10)));
            System.out.printf("After calculating total tax: %d %s%n", total.getAmount(), total.getCurrency());
          });
    }
  }
}