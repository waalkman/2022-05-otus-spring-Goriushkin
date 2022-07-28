package com.study.spring.library.io;

import java.util.Collection;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class Printer<T> {

  protected final LineWriter lineWriter;

  public void print(@NotNull Optional<T> entity) {
    if (entity.isPresent()) {
      printHeader();
      printSingleEntity(entity.get());
    } else {
      printEntityNotFound();
    }

  }
  public void print(Collection<@NotNull T> entities) {
    printHeader();
    entities.forEach(this::printSingleEntity);
  }
  protected abstract void printSingleEntity(T entity);
  protected abstract void printEntityNotFound();
  protected abstract void printHeader();

}
