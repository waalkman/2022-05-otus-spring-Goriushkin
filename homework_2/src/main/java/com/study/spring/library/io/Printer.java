package com.study.spring.library.io;

import java.util.Collection;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class Printer<T> {

  protected final LineWriter lineWriter;

  public void print(@NotNull T entity) {
    printHeader();
    printSingleEntity(entity);
  }
  public void print(Collection<@NotNull T> entities) {
    printHeader();
    entities.forEach(this::printSingleEntity);
  }
  protected abstract void printSingleEntity(T entity);
  protected abstract void printHeader();

}
