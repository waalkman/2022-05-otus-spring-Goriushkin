package com.study.spring.library.map;

public interface EntityMapper<I, O> {

  O map(I entity);

}
