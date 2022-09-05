package com.study.spring.library.changelog;

import com.study.spring.library.domain.Author;
import com.study.spring.library.domain.Book;
import com.study.spring.library.domain.Comment;
import com.study.spring.library.domain.Genre;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;

@ChangeUnit(id = "init-entities", order = "001", author = "spg", runAlways = true)
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
    author1 = Author.builder().name("Женя").build();
    template.save(author1);
    template.save(Author.builder().name("Ваня").build());
    author2 = Author.builder().name("Маня").build();
    template.save(author2);
    template.save(Author.builder().name("Таня").build());
    template.save(Author.builder().name("Софья").build());
  }

  public void initGenres() {
    genre1 = Genre.builder().name("конь").build();
    template.save(genre1);
    genre2 = Genre.builder().name("25").build();
    template.save(genre2);
    template.save(Genre.builder().name("телефон").build());
    template.save(Genre.builder().name("воздух").build());
  }

  public void initBooks() {
    Book book1 = Book.builder()
                      .title("Как понять слона")
                      .description("Слоны для чайников")
                      .author(author1)
                      .genre(genre2)
                      .build();

    template.save(book1);

    book = Book.builder()
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
            new Comment("Очень интересно, буду читать еще", "Матроскин"),
            new Comment("Продам гараж, отдам котят", "Газон")));

    template.save(book);
  }
}
