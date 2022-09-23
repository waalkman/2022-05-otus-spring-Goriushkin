package com.study.spring.batch.config;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;

@RequiredArgsConstructor
public class DelegatingJpaListItemWriter<T> implements ItemWriter<List<T>> {

  private final ItemWriter<T> delegate;

  @Override
  public void write(List<? extends List<T>> items) throws Exception {
    if (!items.isEmpty()) {
      List<T> flatItems = items.stream().flatMap(Collection::stream).collect(Collectors.toList());
      delegate.write(flatItems);
    }
  }
}
