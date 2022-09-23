package com.study.spring.batch.config;

import com.study.spring.batch.mongo.model.Book;
import com.study.spring.batch.postgre.dao.AuthorDaoPG;
import com.study.spring.batch.postgre.dao.GenreDaoPG;
import com.study.spring.batch.postgre.domain.Genre;
import com.study.spring.batch.postgre.exceptions.ConsistencyException;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.lang.NonNull;

@Slf4j
@Configuration
public class BooksStep {

  @Autowired
  private StepBuilderFactory stepBuilderFactory;
  @Autowired
  private MongoTemplate mongoTemplate;
  @Autowired
  private EntityManagerFactory entityManagerFactory;
  @Autowired
  private AuthorDaoPG authorDaoPG;
  @Autowired
  private GenreDaoPG genreDaoPG;

  @Bean("booksStepBean")
  public Step booksStep(
      ItemReader<Book> mongoBookReader,
      ItemWriter<com.study.spring.batch.postgre.domain.Book> postgresBookWriter,
      ItemProcessor<Book, com.study.spring.batch.postgre.domain.Book> bookProcessor) {

    return stepBuilderFactory.get("booksStep")
                             .<Book, com.study.spring.batch.postgre.domain.Book>chunk(5)
                             .reader(mongoBookReader)
                             .processor(bookProcessor)
                             .writer(postgresBookWriter)
                             .listener(
                                 new ItemReadListener<>() {
                                   public void beforeRead() {
                                     log.info("Start reading books");
                                   }

                                   public void afterRead(@NonNull Book o) {
                                     log.info("Read book: {}", o);
                                   }

                                   public void onReadError(@NonNull Exception e) {
                                     log.error("Book read error", e);
                                   }
                                 })
                             .build();
  }

  @Bean
  public ItemProcessor<Book, com.study.spring.batch.postgre.domain.Book> bookProcessor() {
    return book -> {
      String authorId = book.getAuthor().getId();
      com.study.spring.batch.postgre.domain.Author pgAuthor =
          authorDaoPG.findByMongoId(authorId)
                     .orElseThrow(() -> new ConsistencyException("Migration error: author not found"));

      String genreId = book.getGenre().getId();
      Genre pgGenre =
          genreDaoPG.findByMongoId(genreId)
                    .orElseThrow(() -> new ConsistencyException("Migration error: genre not found"));

      return com.study.spring.batch.postgre.domain.Book.builder()
                                                       .title(book.getTitle())
                                                       .description(book.getDescription())
                                                       .author(pgAuthor)
                                                       .genre(pgGenre)
                                                       .mongoId(book.getId())
                                                       .build();
    };
  }

  @Bean
  public ItemReader<Book> mongoBookReader() {
    return new MongoItemReaderBuilder<Book>().name("Book reader")
                                             .template(mongoTemplate)
                                             .collection("books")
                                             .targetType(Book.class)
                                             .jsonQuery("{}")
                                             .sorts(Map.of("title", Direction.ASC))
                                             .build();
  }

  @Bean
  public ItemWriter<com.study.spring.batch.postgre.domain.Book> postgresBookWriter() {
    JpaItemWriter<com.study.spring.batch.postgre.domain.Book> jpaItemWriter = new JpaItemWriter<>();
    jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
    return jpaItemWriter;
  }
}
