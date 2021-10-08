package org.jwebppy.platform.mgmt.logging.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogSearchDto;
import org.jwebppy.platform.mgmt.logging.entity.DataAccessLogEntity;
import org.jwebppy.platform.mgmt.logging.entity.DataAccessLogParameterDetailEntity;
import org.jwebppy.platform.mgmt.logging.entity.DataAccessLogParameterEntity;
import org.jwebppy.platform.mgmt.logging.entity.DataAccessResultLogEntity;
import org.jwebppy.platform.mgmt.logging.entity.DataAccessResultLogParameterDetailEntity;
import org.jwebppy.platform.mgmt.logging.entity.DataAccessResultLogParameterEntity;

@NoLogging
@Mapper
public interface DataAccessLogMapper
{
	public void insertDataAccessLog(DataAccessLogEntity dataAccessLog);
	public void insertDataAccessLogParameter(DataAccessLogParameterEntity dataAccessLogParameter);
	public void insertDataAccessLogParameterDetail(DataAccessLogParameterDetailEntity dataAccessLogParameterDetail);
	public void insertDataAccessResultLog(DataAccessResultLogEntity dataAccessResultLog);
	public void insertDataAccessResultLogParameter(DataAccessResultLogParameterEntity dataAccessResultLogParameter);
	public void insertDataAccessResultLogParameterDetail(DataAccessResultLogParameterDetailEntity dataAccessResultLogParameterDetail);
	public List<DataAccessLogEntity> findPageableLogs(DataAccessLogSearchDto dataAccessLogSearcg);
	public DataAccessLogEntity findLog(String dlSeq);
}
