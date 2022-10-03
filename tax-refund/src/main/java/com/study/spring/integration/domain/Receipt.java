package com.study.spring.integration.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Receipt {
  private Integer amount;
  private Currency currency;
}
