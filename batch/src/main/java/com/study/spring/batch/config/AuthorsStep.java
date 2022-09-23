package com.study.spring.batch.config;

import com.study.spring.batch.mongo.model.Author;
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
public class AuthorsStep {

  @Autowired
  private StepBuilderFactory stepBuilderFactory;
  @Autowired
  private MongoTemplate mongoTemplate;
  @Autowired
  private EntityManagerFactory entityManagerFactory;

  @Bean("authorsStepBean")
  public Step authorsStep(
      ItemReader<Author> mongoAuthorReader,
      ItemWriter<com.study.spring.batch.postgre.domain.Author> postgresAuthorWriter,
      ItemProcessor<Author, com.study.spring.batch.postgre.domain.Author> authorProcessor) {

    return stepBuilderFactory.get("authorsStep")
                             .<Author, com.study.spring.batch.postgre.domain.Author>chunk(5)
                             .reader(mongoAuthorReader)
                             .processor(authorProcessor)
                             .writer(postgresAuthorWriter)
                             .listener(
                                 new ItemReadListener<>() {
                                   public void beforeRead() {
                                     log.info("Start reading authors");
                                   }

                                   public void afterRead(@NonNull Author o) {
                                     log.info("Read author: {}", o);
                                   }

                                   public void onReadError(@NonNull Exception e) {
                                     log.error("Author read error", e);
                                   }
                                 })
                             .build();
  }

  @Bean
  public ItemProcessor<Author, com.study.spring.batch.postgre.domain.Author> authorProcessor() {
    return author ->
        com.study.spring.batch.postgre.domain.Author.builder()
                                                    .name(author.getName())
                                                    .mongoId(author.getId())
                                                    .build();
  }

  @Bean
  public ItemReader<Author> mongoAuthorReader() {
    return new MongoItemReaderBuilder<Author>().name("Author reader")
                                               .template(mongoTemplate)
                                               .collection("authors")
                                               .targetType(Author.class)
                                               .jsonQuery("{}")
                                               .sorts(Map.of("name", Direction.ASC))
                                               .build();
  }

  @Bean
  public ItemWriter<com.study.spring.batch.postgre.domain.Author> postgresAuthorWriter() {
    JpaItemWriter<com.study.spring.batch.postgre.domain.Author> jpaItemWriter = new JpaItemWriter<>();
    jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
    return jpaItemWriter;
  }
}
