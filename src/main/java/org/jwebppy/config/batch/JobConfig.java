package org.jwebppy.config.batch;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogDto;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogSearchDto;
import org.jwebppy.platform.mgmt.logging.service.DataAccessLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

//@Configuration
//@EnableBatchProcessing
public class JobConfig
{
	private static final Logger logger = LoggerFactory.getLogger(JobConfig.class);

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private DataAccessLogService dataAccessLogService;

	@Bean
	public Job job()
	{
		return jobBuilderFactory.get("job")
				.preventRestart()//동일 파라미터로 반복실행이 필요할 경우 주석처리
				.start(clearIfLogStep())
				.build();
	}

	/******************************************************************************************
	 * I/F Log 정리
	 *****************************************************************************************/
	@Bean
	public Step clearIfLogStep()
	{
		return stepBuilderFactory.get("clearIfLogStep")
				.<DataAccessLogDto, DataAccessLogDto>chunk(1000)
				.reader(clearIfLogReader())
				.processor(clearIfLogProcessor())
				.writer(clearIfLogWriter())
				.build();
	}

	@Bean
	public ListItemReader<DataAccessLogDto> clearIfLogReader()
	{
		LocalDateTime toDate = LocalDateTime.now().minusMonths(1);

		DataAccessLogSearchDto dataAccessLogSearch = new DataAccessLogSearchDto();
		dataAccessLogSearch.setToDate(toDate);
		dataAccessLogSearch.setRowPerPage(1000000);

		return new ListItemReader<>(ListUtils.emptyIfNull(dataAccessLogService.getPageableLogs(dataAccessLogSearch)));
	}

	@Bean
	public ItemProcessor<DataAccessLogDto, DataAccessLogDto> clearIfLogProcessor()
	{
		return new ItemProcessor<DataAccessLogDto, DataAccessLogDto>()
		{
			@Override
			public DataAccessLogDto process(DataAccessLogDto dataAccessLog) throws Exception
			{
				logger.info(dataAccessLog.getDlSeq());
				return null;
			}

		};
	}

	@Bean
	public ItemWriter<DataAccessLogDto> clearIfLogWriter()
	{
		return new ItemWriter<DataAccessLogDto>()
		{
			@Override
			public void write(List<? extends DataAccessLogDto> items) throws Exception
			{
			}
		};
	}
}
