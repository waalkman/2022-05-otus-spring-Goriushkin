package com.study.spring.integration.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TotalTax {
  private Integer sum;
  private Currency currency;
}
