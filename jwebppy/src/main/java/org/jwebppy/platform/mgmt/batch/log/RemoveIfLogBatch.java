package org.jwebppy.platform.mgmt.batch.log;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogDto;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogSearchDto;
import org.jwebppy.platform.mgmt.logging.service.DataAccessLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RemoveIfLogBatch
{
	private static final Logger logger = LoggerFactory.getLogger(RemoveIfLogBatch.class);

	private final String BATCH_NAME = "REMOVE_IF_LOG";

	@Autowired
    private JobBuilderFactory jobBuilderFactory;

	@Autowired
    private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private DataAccessLogService dataAccessLogService;

	@Bean
	public Job job()
	{
		return jobBuilderFactory.get(BATCH_NAME)
				//.preventRestart()
				.start(step())
				.incrementer(new ParamCleanRunIdIncrementer())//다른 파라미터로 반복 실행이 필요할 경우
				//.incrementer(new RunIdIncrementer())//동일 파라미터로 반복 실행이 필요할 경우
				.build();
	}

	@Bean
	public Step step()
	{
		return stepBuilderFactory.get(BATCH_NAME + "_STEP")
				.<DataAccessLogDto, DataAccessLogDto>chunk(100)
				.reader(reader(new Date()))
				.processor(processor())
				.writer(writer())
				.build();
	}

	@Bean
	public ListItemReader<DataAccessLogDto> reader(@Value("#{jobParameters[toDate]}") Date today)
	{
		LocalDateTime toDate = Instant.ofEpochMilli(today.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();

		DataAccessLogSearchDto dataAccessLogSearch = new DataAccessLogSearchDto();
		dataAccessLogSearch.setToDate(toDate);
		dataAccessLogSearch.setRowPerPage(1000000);

		return new ListItemReader<>(dataAccessLogService.getPageableLogs(dataAccessLogSearch));
	}

	@Bean
	public ItemProcessor<DataAccessLogDto, DataAccessLogDto> processor()
	{
		return new ItemProcessor<DataAccessLogDto, DataAccessLogDto>()
		{
			@Override
			public DataAccessLogDto process(DataAccessLogDto dataAccessLog) throws Exception
			{
				logger.info(Long.toString(dataAccessLog.getDlSeq()));
				return null;
			}

		};
	}

	@Bean
	public ItemWriter<DataAccessLogDto> writer()
	{
		return new ItemWriter<DataAccessLogDto>()
		{
			@Override
			public void write(List<? extends DataAccessLogDto> items) throws Exception
			{
			}
		};
	}

	/**
	 * 파라미터를 복사하지 않는 RunIdIncrementer
	 */
	public class ParamCleanRunIdIncrementer implements JobParametersIncrementer
	{
		private final String RUN_ID_KEY = "run.id";
		private String key = RUN_ID_KEY;

		public void setKey(String key)
		{
			this.key = key;
		}

		@Override
		public JobParameters getNext(JobParameters parameters)
		{
			return new JobParametersBuilder().addLong(key, System.currentTimeMillis()).toJobParameters(); //이부분이 RunIdIncrementer와 다르다.
		}
	}
}
