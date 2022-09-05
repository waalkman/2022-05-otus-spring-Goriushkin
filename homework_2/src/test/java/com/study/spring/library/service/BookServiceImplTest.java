package com.study.spring.library.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.study.spring.library.domain.Book;
import com.study.spring.library.exceptions.EntityNotFoundException;
import io.mongock.runner.springboot.EnableMongock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@EnableMongock
@SpringBootTest
class BookServiceImplTest {

  @Autowired
  private BookService bookService;

  @Test
  void getAll_success() {
    Flux<Book> books = bookService.findAll();

    StepVerifier.create(books)
                .expectNextCount(2)
                .verifyComplete();
  }

  @Test
  void create_success() {
    String genreName = "25";
    String authorName = "Таня";
    Book book = Book.builder()
                    .title("t")
                    .description("d")
                    .build();

    Mono<Book> createdBook = bookService.create(book, genreName, authorName);

    StepVerifier.create(createdBook)
                .expectNextCount(1)
                .verifyComplete();
  }


  @Test
  void findById_success() {
    Book book = bookService.findAll()
                           .blockFirst();

    Mono<Book> foundByid = bookService.findById(book.getId());

    StepVerifier.create(foundByid)
                .expectNext(book)
                .verifyComplete();
  }

  @Test
  void findById_notFound() {
    Mono<Book> foundByid = bookService.findById("123123");

    StepVerifier.create(foundByid)
                .verifyComplete();
  }

  @Test
  void findByTitle_success() {
    String title = "Как понять слона";
    Mono<Book> foundByTitle = bookService.findByTitle(title);

    StepVerifier.create(foundByTitle)
                .expectNextCount(1)
                .verifyComplete();
  }

  @Test
  void findByTitle_notFound() {
    Mono<Book> foundByTitle = bookService.findByTitle("asefareg3a");

    StepVerifier.create(foundByTitle)
                .verifyComplete();
  }

  @Test
  void update_success() {
    Book book = bookService.findAll()
                           .blockFirst();

    String bookId = book.getId();
    String genreName = "воздух";
    String authorName = "Софья";
    Book bookToUpdate = Book.builder()
                            .id(bookId)
                            .title("t")
                            .description("d")
                            .build();

    Mono<Book> updatedBook = bookService.update(bookToUpdate, genreName, authorName);

    StepVerifier.create(updatedBook)
                .assertNext(b -> {
                  assertEquals(authorName, b.getAuthor()
                                            .getName());
                  assertEquals(genreName, b.getGenre()
                                           .getName());
                })
                .verifyComplete();
  }

  @Test
  void update_notFound() {
    Book book = bookService.findAll()
                           .blockFirst();

    Mono<Book> updatedBook = bookService.update(book, "asd", "dsfsdf");

    StepVerifier.create(updatedBook)
                .verifyError(EntityNotFoundException.class);

  }

  @Test
  void deleteById_success() {
    Book book = createBook();

    Mono<Void> deleted = bookService.deleteById(book.getId());

    StepVerifier.create(deleted)
                .verifyComplete();
  }

  private Book createBook() {
    String genreName = "25";
    String authorName = "Софья";
    Book book = Book.builder()
                    .title("t")
                    .description("d")
                    .build();

    return bookService.create(book, genreName, authorName).block();
  }

}