package com.study.spring.batch.mongo.init;

import com.study.spring.batch.mongo.model.Author;
import com.study.spring.batch.mongo.model.Book;
import com.study.spring.batch.mongo.model.Comment;
import com.study.spring.batch.mongo.model.Genre;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;

@ChangeUnit(id = "init-entities", order = "001", author = "spg", runAlways = true, transactional = false)
@RequiredArgsConstructor
public class InitialEntities {

  private final MongoTemplate template;

  private Genre genre1;
  private Genre genre2;
  private Author author1;
  private Author author2;
  private Book book;

  @Execution
  public void init() {
    dropAll();

    initAuthors();
    initGenres();
    initBooks();
    initComments();
  }

  @RollbackExecution
  public void rollback() {
    dropAll();
  }

  private void dropAll() {
    template.dropCollection(Book.class);
    template.dropCollection(Author.class);
    template.dropCollection(Genre.class);
  }

  public void initAuthors() {
    author1 = Author.builder().id("0000a0000b0000c0000d0000").name("Женя").build();
    template.save(author1);
    template.save(Author.builder().id("0000a0000b0000c0000d0001").name("Ваня").build());
    author2 = Author.builder().id("0000a0000b0000c0000d0002").name("Маня").build();
    template.save(author2);
    template.save(Author.builder().id("0000a0000b0000c0000d0003").name("Таня").build());
    template.save(Author.builder().id("0000a0000b0000c0000d0004").name("Софья").build());
  }

  public void initGenres() {
    genre1 = Genre.builder().id("0000a0000b0000c0000d0005").name("конь").build();
    template.save(genre1);
    genre2 = Genre.builder().id("0000a0000b0000c0000d0006").name("25").build();
    template.save(genre2);
    template.save(Genre.builder().id("0000a0000b0000c0000d0007").name("телефон").build());
    template.save(Genre.builder().id("0000a0000b0000c0000d0008").name("воздух").build());
  }

  public void initBooks() {
    Book book1 = Book.builder()
                      .id("0000a0000b0000c0000d0009")
                      .title("Как понять слона")
                      .description("Слоны для чайников")
                      .author(author1)
                      .genre(genre2)
                      .build();

    template.save(book1);

    book = Book.builder()
               .id("0000a0000b0000c0000d0010")
               .title("Руководство по завариванию топора")
               .description("Кулинарная книга для колобков")
               .author(author2)
               .genre(genre1)
               .build();

    template.save(book);
  }

  public void initComments() {
    book.setComments(
        List.of(
            Comment.builder()
                   .id("0000a0000b0000c0000d0011")
                   .text("Очень интересно, буду читать еще")
                   .userName("Матроскин")
                   .build(),
            Comment.builder()
                   .id("0000a0000b0000c0000d0012")
                   .text("Продам гараж, отдам котят")
                   .userName("Газон")
                   .build()));

    template.save(book);
  }

}
