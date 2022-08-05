package com.study.spring.library.dto;

import com.study.spring.library.domain.Book;
import com.study.spring.library.domain.Comment;
import java.util.Collection;
import lombok.Data;

@Data
public class CommentedBook {

  private final Book book;
  private final Collection<Comment> comments;

  public CommentedBook(Book book, Collection<Comment> comments) {
    this.book = book;
    this.comments = comments;
  }
}
