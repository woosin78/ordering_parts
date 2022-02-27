package org.jwebppy.platform.mgmt.logging.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.SimpleRfcTemplate;
import org.jwebppy.platform.core.service.GeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogDto;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogParameterDetailDto;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogParameterDto;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogSearchDto;
import org.jwebppy.platform.mgmt.logging.dto.ParameterType;
import org.jwebppy.platform.mgmt.logging.entity.DataAccessLogEntity;
import org.jwebppy.platform.mgmt.logging.entity.DataAccessLogParameterDetailEntity;
import org.jwebppy.platform.mgmt.logging.entity.DataAccessLogParameterEntity;
import org.jwebppy.platform.mgmt.logging.mapper.DataAccessLogMapper;
import org.jwebppy.platform.mgmt.logging.mapper.DataAccessLogObjectMapper;
import org.jwebppy.platform.mgmt.logging.mapper.DataAccessLogParameterDetailObjectMapper;
import org.jwebppy.platform.mgmt.logging.mapper.DataAccessLogParameterObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DataAccessLogService extends GeneralService
{
	@Autowired
	private DataAccessLogMapper dataAccessLogMapper;

	@Autowired(required = false)
	private SimpleRfcTemplate simpleRfcTemplate;

	@Async("threadPoolTaskExecutor")
	public void writeLog(DataAccessLogDto dataAccessLog)
	{
		DataAccessLogEntity dataAccessLogEntity = CmModelMapperUtils.mapToEntity(DataAccessLogObjectMapper.INSTANCE, dataAccessLog);

		//Insert Header
		dataAccessLogMapper.insertDataAccessLog(dataAccessLogEntity);

		if (CollectionUtils.isNotEmpty(dataAccessLog.getDataAccessLogParameters()))
		{
			for (DataAccessLogParameterDto dataAccessLogParameter : dataAccessLog.getDataAccessLogParameters())
			{
				DataAccessLogParameterEntity dataAccessLogParameterEntity = CmModelMapperUtils.mapToEntity(DataAccessLogParameterObjectMapper.INSTANCE, dataAccessLogParameter);

				dataAccessLogParameterEntity.setDlSeq(dataAccessLog.getDlSeq());

				dataAccessLogMapper.insertDataAccessLogParameter(dataAccessLogParameterEntity);

				if (CollectionUtils.isNotEmpty(dataAccessLogParameter.getDataAccessLogParameterDetails()))
				{
					for (DataAccessLogParameterDetailDto dataAccessLogParameterDetail : dataAccessLogParameter.getDataAccessLogParameterDetails())
					{
						if (CmStringUtils.isEmpty(dataAccessLogParameterDetail.getValue()))
						{
							continue;
						}

						DataAccessLogParameterDetailEntity dataAccessLogParameterDetailEntity = CmModelMapperUtils.mapToEntity(DataAccessLogParameterDetailObjectMapper.INSTANCE, dataAccessLogParameterDetail);

						dataAccessLogParameterDetailEntity.setDlpSeq(dataAccessLogParameterEntity.getDlpSeq());
						dataAccessLogMapper.insertDataAccessLogParameterDetail(dataAccessLogParameterDetailEntity);
					}
				}
			}
		}
	}

	public void modifyDataAccessLog(DataAccessLogDto dataAccessLog)
	{
		dataAccessLogMapper.updateDataAccessLog(CmModelMapperUtils.mapToEntity(DataAccessLogObjectMapper.INSTANCE, dataAccessLog));
	}

	public List<DataAccessLogDto> getPageableLogs(DataAccessLogSearchDto dataAccessLogSearch)
	{
		return CmModelMapperUtils.mapToDto(DataAccessLogObjectMapper.INSTANCE, dataAccessLogMapper.findPageableLogs(dataAccessLogSearch));
	}

	public DataAccessLogDto getLog(String dlSeq)
	{
		return CmModelMapperUtils.mapToDto(DataAccessLogObjectMapper.INSTANCE, dataAccessLogMapper.findLog(dlSeq));
	}

	public String execute(String dlSeq)
	{
		DataAccessLogDto dataAccessLog = getLog(dlSeq);

		RfcRequest rfcRequest = new RfcRequest(dataAccessLog.getCommand());

		List<DataAccessLogParameterDto> dataAccessLogParameters = dataAccessLog.getDataAccessLogParameters();

		if (CollectionUtils.isNotEmpty(dataAccessLogParameters))
		{
			for (DataAccessLogParameterDto dataAccessLogParameter: dataAccessLogParameters)
			{
				ParameterType type = dataAccessLogParameter.getType();
				String name = dataAccessLogParameter.getName();
				List<DataAccessLogParameterDetailDto> dataAccessLogParameterDetails = dataAccessLogParameter.getDataAccessLogParameterDetails();

				if (CollectionUtils.isNotEmpty(dataAccessLogParameterDetails))
				{
					if (ParameterType.T.equals(type))
					{
						Map<String, Object> valueMap = new HashMap<>();
						List<Map<String, Object>> valueList = new ArrayList<>();
						int lineNo = dataAccessLogParameterDetails.get(0).getLineNo();
						int i = 0;
						int size = dataAccessLogParameterDetails.size();

						do
						{
							DataAccessLogParameterDetailDto dataAccessLogParameterDetail = dataAccessLogParameterDetails.get(i++);

							if (lineNo != dataAccessLogParameterDetail.getLineNo())
							{
								valueList.add(valueMap);

								valueMap = new HashMap<>();

								lineNo = dataAccessLogParameterDetail.getLineNo();
							}

							valueMap.put(dataAccessLogParameterDetail.getName(), dataAccessLogParameterDetail.getValue());
						}
						while (size > i);

						valueList.add(valueMap);

						rfcRequest.addTable(name, valueList);
					}
					else
					{
						for (DataAccessLogParameterDetailDto dataAccessLogParameterDetail: dataAccessLogParameterDetails)
						{
							if (ParameterType.F.equals(type))
							{
								rfcRequest.addField(name, dataAccessLogParameterDetail.getValue());
							}
							else if (ParameterType.S.equals(type))
							{
								rfcRequest.addStructure(name, dataAccessLogParameterDetail.getName(), dataAccessLogParameterDetail.getValue());
							}
						}
					}
				}
			}
		}

		return simpleRfcTemplate.response(rfcRequest).getDlSeq();
	}
}
