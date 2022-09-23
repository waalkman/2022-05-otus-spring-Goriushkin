package com.study.spring.batch.shell;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@Slf4j
@ShellComponent
@RequiredArgsConstructor
public class AppCommands {

  private final Job migrationJob;
  private final JobLauncher jobLauncher;

  @ShellMethod(key = {"migrate", "m"}, value = "Run migration")
  public void runMigration() throws
                             JobInstanceAlreadyCompleteException,
                             JobExecutionAlreadyRunningException,
                             JobParametersInvalidException,
                             JobRestartException {

    JobExecution execution = jobLauncher.run(
        migrationJob,
        new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters());

    log.info("{}", execution);
  }

}
