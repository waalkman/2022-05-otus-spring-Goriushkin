package com.study.spring.library.dao;

import com.study.spring.library.domain.Author;
import com.study.spring.library.domain.Book;
import com.study.spring.library.domain.Comment;
import com.study.spring.library.domain.Genre;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

class DaoTestUtils {

  static final String TEST_BOOK_TITLE = "book title";
  static final String TEST_BOOK_DESCR = "book description";
  static final String TEST_AUTHOR = "test_a";
  static final String TEST_GENRE = "test_g";

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
                          .name(TEST_AUTHOR)
                          .build();

    return em.persistAndFlush(author);
  }

  static Genre createGenre(TestEntityManager em) {
    Genre genre = Genre.builder()
                       .name(TEST_GENRE)
                       .build();

    return em.persistAndFlush(genre);
  }

  static Comment createComment(TestEntityManager em) {
    Comment comment = Comment.builder()
                              .text("Comment text")
                              .userName("Comment author")
                              .book(createBook(em, TEST_BOOK_TITLE, TEST_BOOK_DESCR))
                              .build();

    return em.persistAndFlush(comment);
  }
}
