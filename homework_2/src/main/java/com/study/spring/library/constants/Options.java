package com.study.spring.library.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Options {

  public static final String CREATE = "create";
  public static final String DELETE_BY_ID = "deleteById";
  public static final String GET_ALL = "getAll";
  public static final String GET_BY_ID = "getById";
  public static final String GET_BY_NAME = "getByName";
  public static final String UPDATE = "update";

  public static final String GET_BY_TITLE = "getByTitle";
  public static final String GET_BY_BOOK_TITLE = "findByBookTitle";

}
