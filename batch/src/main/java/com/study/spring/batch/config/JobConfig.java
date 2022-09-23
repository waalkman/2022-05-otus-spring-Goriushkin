package com.study.spring.batch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

@Configuration
@Slf4j
@EnableBatchProcessing
public class JobConfig {

  @Autowired
  private JobBuilderFactory jobBuilderFactory;

  @Bean
  public Job migrateDataJob(
      @Qualifier("authorsStepBean") Step authorsStep,
      @Qualifier("genresStepBean") Step genresStep,
      @Qualifier("booksStepBean") Step booksStep,
      @Qualifier("commentsStepBean") Step commentsStep) {

    return jobBuilderFactory.get("Migrate data")
                            .incrementer(new RunIdIncrementer())
                            .flow(authorsStep)
                            .next(genresStep)
                            .next(booksStep)
                            .next(commentsStep)
                            .end()
                            .listener(new JobExecutionListener() {
                              @Override
                              public void beforeJob(@NonNull JobExecution jobExecution) {
                                log.info("Start migration");
                              }
                              @Override
                              public void afterJob(@NonNull JobExecution jobExecution) {
                                log.info("End migration");
                              }
                            })
                            .build();
  }

}
