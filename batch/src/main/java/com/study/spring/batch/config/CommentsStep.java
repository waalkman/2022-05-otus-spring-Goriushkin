package com.study.spring.batch.config;

import com.study.spring.batch.mongo.model.Book;
import com.study.spring.batch.mongo.model.Comment;
import com.study.spring.batch.postgre.dao.AuthorDaoPG;
import com.study.spring.batch.postgre.dao.BookDaoPG;
import com.study.spring.batch.postgre.dao.GenreDaoPG;
import com.study.spring.batch.postgre.domain.Genre;
import com.study.spring.batch.postgre.exceptions.ConsistencyException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;
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
import org.springframework.util.CollectionUtils;

@Slf4j
@Configuration
public class CommentsStep {

  @Autowired
  private StepBuilderFactory stepBuilderFactory;
  @Autowired
  private MongoTemplate mongoTemplate;
  @Autowired
  private EntityManagerFactory entityManagerFactory;
  @Autowired
  private BookDaoPG bookDaoPG;

  @Bean("commentsStepBean")
  public Step commentsStep(
      ItemReader<Book> mongoBookReaderForComments,
      ItemWriter<List<com.study.spring.batch.postgre.domain.Comment>> postgresCommentListWriter,
      ItemProcessor<Book, List<com.study.spring.batch.postgre.domain.Comment>> commentProcessor) {

    return stepBuilderFactory.get("commentsStep")
                             .<Book, List<com.study.spring.batch.postgre.domain.Comment>>chunk(5)
                             .reader(mongoBookReaderForComments)
                             .processor(commentProcessor)
                             .writer(postgresCommentListWriter)
                             .listener(
                                 new ItemReadListener<>() {
                                   @Override
                                   public void beforeRead() {
                                     log.info("Start reading books");
                                   }
                                   @Override
                                   public void afterRead(Book o) {
                                     log.info("Read book: {}", o);
                                   }
                                   @Override
                                   public void onReadError(Exception e) {
                                     log.error("Book read error", e);
                                   }
                                 })
                             .build();
  }

  @Bean
  public ItemProcessor<Book, List<com.study.spring.batch.postgre.domain.Comment>> commentProcessor() {
    return book -> {
      com.study.spring.batch.postgre.domain.Book pgBook =
          bookDaoPG.findByMongoId(book.getId())
                   .orElseThrow(() -> new ConsistencyException("Migration error: author not found"));

      List<Comment> comments = book.getComments();
      if (CollectionUtils.isEmpty(comments)) {
        return Collections.emptyList();
      } else {
        return mapComments(book.getComments(), pgBook);
      }
    };
  }

  private List<com.study.spring.batch.postgre.domain.Comment> mapComments(
      List<Comment> comments,
      com.study.spring.batch.postgre.domain.Book pgBook) {

    return
        comments.stream()
                .map(comment ->
                         com.study.spring.batch.postgre.domain.Comment.builder()
                                                                      .book(pgBook)
                                                                      .text(comment.getText())
                                                                      .userName(comment.getUserName())
                                                                      .mongoId(comment.getId())
                                                                      .build())
                .collect(Collectors.toList());
  }

  @Bean
  public ItemReader<Book> mongoBookReaderForComments() {
    return new MongoItemReaderBuilder<Book>().name("Book reader")
                                             .template(mongoTemplate)
                                             .collection("books")
                                             .targetType(Book.class)
                                             .jsonQuery("{}")
                                             .sorts(Map.of("title", Direction.ASC))
                                             .build();
  }

  @Bean
  public ItemWriter<com.study.spring.batch.postgre.domain.Comment> postgresCommentWriter() {
    JpaItemWriter<com.study.spring.batch.postgre.domain.Comment> jpaItemWriter = new JpaItemWriter<>();
    jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
    return jpaItemWriter;
  }

  @Bean
  public ItemWriter<List<com.study.spring.batch.postgre.domain.Comment>> postgresCommentListWriter(
      ItemWriter<com.study.spring.batch.postgre.domain.Comment> postgresCommentWriter) {

    return new DelegatingJpaListItemWriter<>(postgresCommentWriter);
  }



}
