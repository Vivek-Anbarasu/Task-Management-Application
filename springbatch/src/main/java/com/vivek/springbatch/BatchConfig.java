package com.vivek.springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

	@Bean
    public Job jobOne(JobRepository jobRepository, Step stepOne) {
        return new JobBuilder("jobOne", jobRepository).start(stepOne) .build();
    }

    @Bean
    public Step stepOne(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("stepOne", jobRepository).tasklet((contribution, chunkContext) -> {
        	System.out.println("Hello");
              return RepeatStatus.FINISHED;
          }, transactionManager)
          .build();
    }

    @Bean
    public Job jobTwo(JobRepository jobRepository, Step stepTwo) {
        return new JobBuilder("jobTwo", jobRepository).start(stepTwo) .build();
    }

    @Bean
    public Step stepTwo(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("stepTwo", jobRepository).tasklet((contribution, chunkContext) -> {
              System.out.println("World");
              return RepeatStatus.FINISHED;
          }, transactionManager)
          .build();
    }
    
    @Bean
    public Job jobThree(JobRepository jobRepository, Step stepThree) {
        return new JobBuilder("jobThree", jobRepository).start(stepThree) .build();
    }

    @Bean
    public Step stepThree(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("stepThree", jobRepository).tasklet((contribution, chunkContext) -> {
              System.out.println("Superb");
              return RepeatStatus.FINISHED;
          }, transactionManager)
          .build();
    }
 
}
