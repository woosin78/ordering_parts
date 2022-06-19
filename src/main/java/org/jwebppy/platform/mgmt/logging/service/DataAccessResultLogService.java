package org.jwebppy.platform.mgmt.logging.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.list.UnmodifiableList;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.service.GeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessResultLogDto;
import org.jwebppy.platform.mgmt.logging.dto.ParameterType;
import org.jwebppy.platform.mgmt.logging.entity.DataAccessResultLogEntity;
import org.jwebppy.platform.mgmt.logging.entity.DataAccessResultLogParameterDetailEntity;
import org.jwebppy.platform.mgmt.logging.entity.DataAccessResultLogParameterEntity;
import org.jwebppy.platform.mgmt.logging.mapper.DataAccessResultLogMapper;
import org.jwebppy.platform.mgmt.logging.mapper.DataAccessResultLogObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DataAccessResultLogService extends GeneralService
{
	@Autowired
	private DataAccessResultLogMapper dataAccessResultLogMapper;

	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	public void writeLog(String dlSeq, RfcResponse rfcResponse)
	{
		if (ObjectUtils.isNotEmpty(rfcResponse))
		{
			Map<String, Object> resultMap = rfcResponse.getResultMap();

			if (MapUtils.isNotEmpty(resultMap))
			{
				SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);

				DataAccessResultLogEntity dataAccessResultLog = new DataAccessResultLogEntity();
				dataAccessResultLog.setDlSeq(dlSeq);

				dataAccessResultLogMapper.insertDataAccessResultLog(dataAccessResultLog);

				Long drlSeq = dataAccessResultLog.getDrlSeq();

				Iterator<String> it = resultMap.keySet().iterator();

				while (it.hasNext())
				{
					String key = CmStringUtils.trimToEmpty(it.next());
					Object value = resultMap.get(key);

					ParameterType type = ParameterType.F;

					if (value instanceof List)
					{
						type = ParameterType.T;
					}
					else if (value instanceof Map)
					{
						type = ParameterType.S;
					}

					DataAccessResultLogParameterEntity dataAccessResultLogParameter = new DataAccessResultLogParameterEntity();
					dataAccessResultLogParameter.setDrlSeq(drlSeq);
					dataAccessResultLogParameter.setType(type);
					dataAccessResultLogParameter.setName(key);

					dataAccessResultLogMapper.insertDataAccessResultLogParameter(dataAccessResultLogParameter);

					Long drlpSeq = dataAccessResultLogParameter.getDrlpSeq();

					if (ParameterType.T.equals(type))
					{
						List<Map<String, Object>> list = UnmodifiableList.unmodifiableList((List)value);

						for (int i=0, size=list.size(); i<size; i++)
						{
							Map<String, Object> subValueMap = list.get(i);

							if (MapUtils.isNotEmpty(subValueMap))
							{
								Iterator<Entry<String, Object>> subIt = subValueMap.entrySet().iterator();

								while (subIt.hasNext())
								{
									Entry<String, Object> subEntry = subIt.next();

									addDataAccessResultLogParameterDetail(sqlSession, drlpSeq, i, subEntry.getKey(), subEntry.getValue());
								}
							}
						}
					}
					else if (ParameterType.S.equals(type))
					{
						Map<String, Object> subValueMap = (Map)value;

						if (MapUtils.isNotEmpty(subValueMap))
						{
							Iterator<Entry<String, Object>> subIt = subValueMap.entrySet().iterator();

							while (subIt.hasNext())
							{
								Entry<String, Object> subEntry = subIt.next();

								addDataAccessResultLogParameterDetail(sqlSession, drlpSeq, 0, subEntry.getKey(), subEntry.getValue());
							}
						}
					}
					else
					{
						addDataAccessResultLogParameterDetail(sqlSession, drlpSeq, 0, key, value);
					}
				}

				sqlSession.flushStatements();
                sqlSession.close();
                sqlSession.clearCache();
			}
		}
	}

	private void addDataAccessResultLogParameterDetail(SqlSession sqlSession, Long drlpSeq, int lineNo, String name, Object value)
	{
		DataAccessResultLogParameterDetailEntity dataAccessResultLogParameterDetail = new DataAccessResultLogParameterDetailEntity();

		dataAccessResultLogParameterDetail.setDrlpSeq(drlpSeq);
		dataAccessResultLogParameterDetail.setLineNo(lineNo);
		dataAccessResultLogParameterDetail.setName(CmStringUtils.trimToEmpty(name));
		dataAccessResultLogParameterDetail.setValue(CmStringUtils.trimToEmpty(value));

		sqlSession.insert("org.jwebppy.platform.mgmt.logging.mapper.DataAccessResultLogMapper.insertDataAccessResultLogParameterDetail", dataAccessResultLogParameterDetail);
	}

	public DataAccessResultLogDto getResultLog(long drlSeq)
	{
		return CmModelMapperUtils.mapToDto(DataAccessResultLogObjectMapper.INSTANCE, dataAccessResultLogMapper.findResultLog(drlSeq));
	}

	public List<DataAccessResultLogDto> getResultLogs(String dlSeq)
	{
		return CmModelMapperUtils.mapToDto(DataAccessResultLogObjectMapper.INSTANCE, dataAccessResultLogMapper.findResultLogs(dlSeq));
	}
}
