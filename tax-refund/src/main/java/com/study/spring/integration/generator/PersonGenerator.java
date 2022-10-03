package com.study.spring.integration.generator;

import com.study.spring.integration.domain.Currency;
import com.study.spring.integration.domain.Person;
import java.util.Collection;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

@Component
public class PersonGenerator {

  public Collection<Person> generate(int size) {
    return Stream.generate(UUID::randomUUID)
                 .limit(size)
                 .map(Object::toString)
                 .map(id -> new Person(id, getCurrency()))
                 .collect(Collectors.toList());
  }

  private Currency getCurrency() {
    return new Random().nextBoolean() ? Currency.BANANA : Currency.COCONUT;
  }
}
