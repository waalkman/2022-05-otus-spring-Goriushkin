package com.study.spring.integration.service;

import com.study.spring.integration.domain.Person;
import com.study.spring.integration.domain.Receipt;
import org.springframework.stereotype.Service;

@Service
public class TaxAmountService {

  public Receipt calculatePersonTax(Person person) throws InterruptedException {
    System.out.printf("Calculating tax for: %s%n", person.getId());
    Thread.sleep(2000L);
    int tax = Math.abs(person.getId().hashCode()) / 100_000;
    System.out.printf("Calculated tax for: %s is: %d %s %n", person.getId(), tax, person.getCurrency());
    return new Receipt(tax, person.getCurrency());
  }

}
