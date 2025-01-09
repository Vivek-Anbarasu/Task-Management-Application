package com.vivek.springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class SpringbatchApplication {

	 @Autowired
	    private Job jobOne;

	    @Autowired
	    private Job jobTwo;

	    @Autowired
	    private JobLauncher jobLauncher;

	    @Scheduled(cron = "${scheduled.job1}")  // Run every 12secs
	    public void runJob1() throws Exception {
	        JobParameters jobParameters = new JobParametersBuilder() .addString("jobID", String.valueOf(System.currentTimeMillis())) .toJobParameters();
	        System.out.println("Executing sheduled job 1");
	        jobLauncher.run(jobOne, jobParameters);
	    }

	    @Scheduled(cron = "${scheduled.job2}")  // Run every 20secs
	    public void runJob2() throws Exception {
	        JobParameters jobParameters = new JobParametersBuilder() .addString("jobID", String.valueOf(System.currentTimeMillis())) .toJobParameters();
	        System.out.println("Executing sheduled job 2");
	        jobLauncher.run(jobTwo, jobParameters);
	    }

	
	public static void main(String[] args) {
		SpringApplication.run(SpringbatchApplication.class, args);
	}

}
