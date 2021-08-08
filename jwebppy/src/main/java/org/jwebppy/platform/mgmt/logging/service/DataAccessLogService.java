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
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogDto;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogParameterDetailDto;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogParameterDto;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogSearchDto;
import org.jwebppy.platform.mgmt.logging.dto.ParameterType;
import org.jwebppy.platform.mgmt.logging.entity.DataAccessLogEntity;
import org.jwebppy.platform.mgmt.logging.entity.DataAccessLogParameterDetailEntity;
import org.jwebppy.platform.mgmt.logging.entity.DataAccessLogParameterEntity;
import org.jwebppy.platform.mgmt.logging.mapper.DataAccessLogMapper;
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

	@Autowired
	private SimpleRfcTemplate simpleRfcTemplate;

	@Async("threadPoolTaskExecutor")
	public void writeLog(DataAccessLogDto dataAccessLog)
	{
		DataAccessLogEntity dataAccessLogEntity = CmModelMapperUtils.map(dataAccessLog, DataAccessLogEntity.class);

		//Insert Header
		dataAccessLogMapper.insertDataAccessLog(dataAccessLogEntity);

		if (CollectionUtils.isNotEmpty(dataAccessLog.getDataAccessLogParameters()))
		{
			for (DataAccessLogParameterDto dataAccessLogParameter : dataAccessLog.getDataAccessLogParameters())
			{
				DataAccessLogParameterEntity dataAccessLogParameterEntity = CmModelMapperUtils.map(dataAccessLogParameter, DataAccessLogParameterEntity.class);

				dataAccessLogParameterEntity.setDlSeq(dataAccessLog.getDlSeq());

				dataAccessLogMapper.insertDataAccessLogParameter(dataAccessLogParameterEntity);

				if (CollectionUtils.isNotEmpty(dataAccessLogParameter.getDataAccessLogParameterDetails()))
				{
					for (DataAccessLogParameterDetailDto dataAccessLogParameterDetail : dataAccessLogParameter.getDataAccessLogParameterDetails())
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

	public DataAccessLogDto getLog(String dlSeq)
	{
		return CmModelMapperUtils.map(dataAccessLogMapper.findLog(dlSeq), DataAccessLogDto.class);
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
