package com.study.spring.batch.config;

import com.study.spring.batch.mongo.model.Genre;
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
public class GenresStep {

  @Autowired
  private StepBuilderFactory stepBuilderFactory;
  @Autowired
  private MongoTemplate mongoTemplate;
  @Autowired
  private EntityManagerFactory entityManagerFactory;

  @Bean("genresStepBean")
  public Step authorsStep(
      ItemReader<Genre> mongoGenreReader,
      ItemWriter<com.study.spring.batch.postgre.domain.Genre> postgresGenreWriter,
      ItemProcessor<Genre, com.study.spring.batch.postgre.domain.Genre> genreProcessor) {

    return stepBuilderFactory.get("genresStep")
                             .<Genre, com.study.spring.batch.postgre.domain.Genre>chunk(5)
                             .reader(mongoGenreReader)
                             .processor(genreProcessor)
                             .writer(postgresGenreWriter)
                             .listener(
                                 new ItemReadListener<>() {
                                   public void beforeRead() {
                                     log.info("Start reading genres");
                                   }

                                   public void afterRead(@NonNull Genre o) {
                                     log.info("Read genre: {}", o);
                                   }

                                   public void onReadError(@NonNull Exception e) {
                                     log.error("Genre read error", e);
                                   }
                                 })
                             .build();
  }

  @Bean
  public ItemProcessor<Genre, com.study.spring.batch.postgre.domain.Genre> genreProcessor() {
    return genre ->
        com.study.spring.batch.postgre.domain.Genre.builder()
                                                    .name(genre.getName())
                                                    .mongoId(genre.getId())
                                                    .build();
  }

  @Bean
  public ItemReader<Genre> mongoGenreReader() {
    return new MongoItemReaderBuilder<Genre>().name("Genre reader")
                                               .template(mongoTemplate)
                                               .collection("genres")
                                               .targetType(Genre.class)
                                               .jsonQuery("{}")
                                               .sorts(Map.of("name", Direction.ASC))
                                               .build();
  }

  @Bean
  public ItemWriter<com.study.spring.batch.postgre.domain.Genre> postgresGenreWriter() {
    JpaItemWriter<com.study.spring.batch.postgre.domain.Genre> jpaItemWriter = new JpaItemWriter<>();
    jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
    return jpaItemWriter;
  }
}
