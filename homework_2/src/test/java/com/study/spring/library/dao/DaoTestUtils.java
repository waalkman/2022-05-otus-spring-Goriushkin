package com.study.spring.library.dao;

import com.study.spring.library.domain.Author;
import com.study.spring.library.domain.Book;
import com.study.spring.library.domain.Comment;
import com.study.spring.library.domain.Genre;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

class DaoTestUtils {

  static Book createBook(TestEntityManager em, String title, String description) {
    Book book = Book.builder()
                    .title(title)
                    .description(description)
                    .genre(createGenre(em))
                    .author(createAuthor(em))
                    .build();

    return em.persistAndFlush(book);
  }

  static Author createAuthor(TestEntityManager em) {
    Author author = Author.builder()
                          .name("test_a")
                          .build();

    return em.persistAndFlush(author);
  }

  static Genre createGenre(TestEntityManager em) {
    Genre genre = Genre.builder()
                       .name("test_g")
                       .build();

    return em.persistAndFlush(genre);
  }

  static Comment createComment(TestEntityManager em) {
    Comment comment = Comment.builder()
                              .text("Comment text")
                              .userName("Comment author")
                              .book(createBook(em, "book title", "book description"))
                              .build();

    return em.persistAndFlush(comment);
  }
}
