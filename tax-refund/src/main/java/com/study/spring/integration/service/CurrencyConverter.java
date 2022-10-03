package com.study.spring.integration.service;

import com.study.spring.integration.domain.Currency;
import com.study.spring.integration.domain.Receipt;
import org.springframework.stereotype.Service;

@Service
public class CurrencyConverter {

  public Receipt convert(Receipt income) throws InterruptedException {
    System.out.printf("Received for conversion: %d %s%n", income.getAmount(), income.getCurrency());
    Thread.sleep(2000);
    Receipt conversionResult;
    if (Currency.COCONUT.equals(income.getCurrency())) {
      conversionResult = new Receipt(Math.round(income.getAmount() * Currency.EXCHANGE_RATE), Currency.BANANA);
    } else {
      conversionResult = income;
    }

    System.out.printf("Converted to bananas: %d %s%n", conversionResult.getAmount(), Currency.BANANA);
    return conversionResult;
  }
}
