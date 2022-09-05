package com.study.spring.library.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.study.spring.library.domain.Book;
import com.study.spring.library.domain.Comment;
import com.study.spring.library.exceptions.EntityNotFoundException;
import io.mongock.runner.springboot.EnableMongock;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@EnableMongock
@SpringBootTest
class CommentServiceImplTest {

  @Autowired
  private BookService bookService;
  @Autowired
  private CommentService commentService;

  @Test
  void create_success() {
    Book book = bookService.findAll()
                           .blockFirst();

    String userName = "testUser";
    String text = "commentText";

    Comment comment = new Comment(text, userName);

    Mono<Book> updatedBook = commentService.create(comment, book.getId());

    StepVerifier.create(updatedBook)
                .assertNext(
                    b -> assertTrue(b.getComments()
                                     .stream()
                                     .anyMatch(c -> userName.equals(c.getUserName()) && text.equals(c.getText()))))
                .verifyComplete();
  }

  @Test
  void create_bookNotFound() {
    String bookId = "2839tbcoiqfg";

    Mono<Book> updatedBook = commentService.create(null, bookId);

    StepVerifier.create(updatedBook)
                .verifyError(EntityNotFoundException.class);
  }

  @Test
  void update_success() {
    Book book = bookService.findAll()
                           .blockFirst();

    Comment c = createCommentForExistingBok(book);

    String newText = "new text";
    c.setText(newText);
    Mono<Book> bookWithComment = commentService.update(c, book.getId());

    StepVerifier.create(bookWithComment)
                .assertNext(b -> assertTrue(b.getComments()
                                             .stream()
                                             .anyMatch(comment -> newText.equals(comment.getText()))))
                .verifyComplete();

  }

  @Test
  void deleteById_success() {
    Book book = bookService.findAll()
                           .blockFirst();

    Comment comment1 = new Comment("text_1", "dsfdsafsdf");
    Comment comment2 = new Comment("text_2", "gflh3qp87gifn;");

    commentService.create(comment1, book.getId())
                  .block();
    commentService.create(comment2, book.getId())
                  .block();

    Mono<Book> updatedBook = commentService.deleteById(book.getId(), comment2.getId());

    StepVerifier.create(updatedBook)
                .assertNext(
                    b -> {
                      assertTrue(b.getComments()
                                  .contains(comment1));
                      assertFalse(b.getComments()
                                   .contains(comment2));
                    })
                .verifyComplete();

  }

  private Comment createCommentForExistingBok(Book book) {
    String text = "uN1qU3 73x7";
    commentService.create(new Comment(text, "dsfdsafsdf"), book.getId())
                  .block();

    return bookService.findById(book.getId())
                      .block()
                      .getComments()
                      .stream()
                      .filter(c -> text.equals(c.getText()))
                      .collect(Collectors.toList())
                      .iterator()
                      .next();
  }
}