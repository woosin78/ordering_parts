package org.jwebppy.platform.mgmt.logging.mapper;

import java.util.List;

import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogSearchDto;
import org.jwebppy.platform.mgmt.logging.entity.DataAccessLogEntity;
import org.jwebppy.platform.mgmt.logging.entity.DataAccessLogParameterDetailEntity;
import org.jwebppy.platform.mgmt.logging.entity.DataAccessLogParameterEntity;
import org.springframework.stereotype.Repository;

@Repository
@NoLogging
public interface DataAccessLogMapper
{
	public void insertDataAccessLog(DataAccessLogEntity dataAccessLog);
	public void insertDataAccessLogParameter(DataAccessLogParameterEntity dataAccessLogParameter);
	public void insertDataAccessLogParameterDetail(DataAccessLogParameterDetailEntity dataAccessLogParameterDetail);
	public List<DataAccessLogEntity> findLogs(DataAccessLogSearchDto dataAccessLogSearcg);
	public DataAccessLogEntity findLog(Long dalSeq);
}
