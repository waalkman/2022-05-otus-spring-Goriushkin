package com.study.spring.library.io;

import java.util.Collection;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class Printer<T> {

  protected final LineWriter lineWriter;

  public abstract void print(T entity);
  public abstract void print(Collection<T> entity);

}
