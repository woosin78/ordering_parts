package org.jwebppy.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

//@Configuration
//@EnableScheduling
public class SchedulerConfig implements SchedulingConfigurer
{
	private static final Logger logger = LoggerFactory.getLogger(SchedulerConfig.class);

	@Value("${task.scheduling.threadPoolSize}")
	private int THREAD_POOL_SIZE;

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;

	@Override
	public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar)
	{
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

		threadPoolTaskScheduler.setPoolSize(THREAD_POOL_SIZE);
		threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler_");
		threadPoolTaskScheduler.initialize();

		scheduledTaskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
	}

	/*
	 * *초(0-59) *분(0-59) *시간(0-23) *일(1-31) *월(1-12) *요일(0-7)
	 */
	@Scheduled(cron = "0 48 16 * * ?")
	public void perform() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException
	{
		JobParameters jobParameters = new JobParametersBuilder()
				//.addLong("startTime", System.currentTimeMillis())//동일 파라미터로 반복실행이 필요할 경우
				.toJobParameters();

		jobLauncher.run(job, jobParameters);
	}
}
