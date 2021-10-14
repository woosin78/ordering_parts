package org.jwebppy.config;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig
{
	private Logger logger = LoggerFactory.getLogger(AsyncConfig.class);

	@Bean(name = "threadPoolTaskExecutor")
	public Executor threadPoolTaskExecutor()
	{
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(30);
		taskExecutor.setMaxPoolSize(60);
		taskExecutor.setQueueCapacity(30);
		taskExecutor.setThreadNamePrefix("Executor-");
		taskExecutor.initialize();

		return new HandlingExecutor(taskExecutor);
	}

	public class HandlingExecutor implements AsyncTaskExecutor
	{
		private AsyncTaskExecutor executor;

		public HandlingExecutor(AsyncTaskExecutor executor)
		{
			this.executor = executor;
		}

		@Override
		public void execute(Runnable task)
		{
			executor.execute(createWrappedRunnable(task));
		}

		@Override
		public void execute(Runnable task, long startTimeout)
		{
			executor.execute(createWrappedRunnable(task), startTimeout);
		}

		@Override
		public Future<?> submit(Runnable task)
		{
			return executor.submit(createWrappedRunnable(task));
		}

		@Override
		public <T> Future<T> submit(final Callable<T> task)
		{
			return executor.submit(createCallable(task));
		}

		private <T> Callable<T> createCallable(final Callable<T> task)
		{
			return new Callable<T>()
			{
				@Override
				public T call() throws Exception
				{
					try
					{
						return task.call();
					}
					catch (Exception e)
					{
						handle(e);
						throw e;
					}
				}
			};
		}

		private Runnable createWrappedRunnable(final Runnable task)
		{
			return new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						task.run();
					}
					catch (Exception e)
					{
						handle(e);
					}
				}
			};
		}

		private void handle(Exception e)
		{
			logger.info("Failed to execute task. : {}", e.getMessage());
			logger.error("Failed to execute task. ", e);
		}

		public void destroy()
		{
			if (executor instanceof ThreadPoolTaskExecutor)
			{
				((ThreadPoolTaskExecutor) executor).shutdown();
			}
		}
	}
}
