package com.study.spring.integration.config;

import com.study.spring.integration.domain.Currency;
import com.study.spring.integration.domain.Receipt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.aggregator.MessageGroupProcessor;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.transformer.GenericTransformer;

@Configuration
public class Flows {

  @Bean
  public DirectChannel inputChannel() {
    return MessageChannels.direct()
                          .get();
  }

  @Bean
  public PublishSubscribeChannel outputChannel() {
    return MessageChannels.publishSubscribe()
                          .get();
  }

  @Bean
  public IntegrationFlow taxFlow() {
    return IntegrationFlows.from("inputChannel")
                           .split()
                           .handle("taxAmountService", "calculatePersonTax")
                           .handle("currencyConverter", "convert")
                           .aggregate(
                               (MessageGroupProcessor) group -> group.getMessages()
                                                                     .stream()
                                                                     .map(
                                                                         obj -> ((Receipt) obj.getPayload()).getAmount())
                                                                     .reduce(0, Integer::sum))
                           .transform(
                               (GenericTransformer<Integer, Receipt>) amount -> new Receipt(amount, Currency.BANANA))
                           .channel("outputChannel")
                           .get();
  }

}
