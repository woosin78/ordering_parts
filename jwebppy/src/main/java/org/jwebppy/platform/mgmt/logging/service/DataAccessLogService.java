package org.jwebppy.platform.mgmt.logging.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.service.GeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogDto;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogParameterDetailDto;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogParameterDto;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogSearchDto;
import org.jwebppy.platform.mgmt.logging.entity.DataAccessLogEntity;
import org.jwebppy.platform.mgmt.logging.entity.DataAccessLogParameterDetailEntity;
import org.jwebppy.platform.mgmt.logging.entity.DataAccessLogParameterEntity;
import org.jwebppy.platform.mgmt.logging.mapper.DataAccessLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DataAccessLogService extends GeneralService
{
	@Autowired
	private DataAccessLogMapper dataAccessLogMapper;

	@Transactional
	//@Async("threadPoolTaskExecutor")
	public void writeLog(DataAccessLogDto dataAccessLog)
	{
		DataAccessLogEntity dataAccessLogEntity = CmModelMapperUtils.map(dataAccessLog, DataAccessLogEntity.class);

		//Insert Header
		dataAccessLogMapper.insertDataAccessLog(dataAccessLogEntity);

		if (CollectionUtils.isNotEmpty(dataAccessLog.getDataAccessLogParameters()))
		{
			for (DataAccessLogParameterDto dataAccessLogParameterDto : dataAccessLog.getDataAccessLogParameters())
			{
				DataAccessLogParameterEntity dataAccessLogParameterEntity = CmModelMapperUtils.map(dataAccessLogParameterDto, DataAccessLogParameterEntity.class);

				dataAccessLogParameterEntity.setDlSeq(dataAccessLogEntity.getDlSeq());
				dataAccessLogMapper.insertDataAccessLogParameter(dataAccessLogParameterEntity);

				if (CollectionUtils.isNotEmpty(dataAccessLogParameterDto.getDataAccessLogParameterDetails()))
				{
					for (DataAccessLogParameterDetailDto dataAccessLogParameterDetail : dataAccessLogParameterDto.getDataAccessLogParameterDetails())
					{
						DataAccessLogParameterDetailEntity dataAccessLogParameterDetailEntity = CmModelMapperUtils.map(dataAccessLogParameterDetail, DataAccessLogParameterDetailEntity.class);

						dataAccessLogParameterDetailEntity.setDlpSeq(dataAccessLogParameterEntity.getDlpSeq());
						dataAccessLogMapper.insertDataAccessLogParameterDetail(dataAccessLogParameterDetailEntity);
					}
				}
			}
		}
	}

	public List<DataAccessLogDto> getPageableLogs(DataAccessLogSearchDto dataAccessLogSearch)
	{
		return CmModelMapperUtils.mapAll(dataAccessLogMapper.findPageableLogs(dataAccessLogSearch), DataAccessLogDto.class);
	}

	public DataAccessLogDto getLog(Long dlSeq)
	{
		return CmModelMapperUtils.map(dataAccessLogMapper.findLog(dlSeq), DataAccessLogDto.class);
	}
}
