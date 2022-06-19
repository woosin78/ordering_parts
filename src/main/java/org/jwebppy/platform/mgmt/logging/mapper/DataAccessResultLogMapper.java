package org.jwebppy.platform.mgmt.logging.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.platform.mgmt.logging.entity.DataAccessResultLogEntity;
import org.jwebppy.platform.mgmt.logging.entity.DataAccessResultLogParameterDetailEntity;
import org.jwebppy.platform.mgmt.logging.entity.DataAccessResultLogParameterEntity;

@NoLogging
@Mapper
public interface DataAccessResultLogMapper
{
	public void insertDataAccessResultLog(DataAccessResultLogEntity dataAccessResultLog);
	public void insertDataAccessResultLogParameter(DataAccessResultLogParameterEntity dataAccessResultLogParameter);
	public void insertDataAccessResultLogParameterDetail(DataAccessResultLogParameterDetailEntity dataAccessResultLogParameterDetail);
	public DataAccessResultLogEntity findResultLog(long dlSeq);
	public List<DataAccessResultLogEntity> findSimpleResultLogs(String dlSeq);
	public List<DataAccessResultLogEntity> findResultLogs(String dlSeq);
}
