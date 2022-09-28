package com.study.spring.integration;

import com.study.spring.integration.domain.Person;
import com.study.spring.integration.domain.Receipt;
import java.util.Collection;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface TaxService {

  @Gateway(requestChannel = "inputChannel", replyChannel = "outputChannel")
  Receipt calculate(Collection<Person> people);

}
