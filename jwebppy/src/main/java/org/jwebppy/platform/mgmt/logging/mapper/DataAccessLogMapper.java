package org.jwebppy.platform.mgmt.logging.mapper;

import java.util.List;

import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.platform.mgmt.logging.dto.DataAccessLogSearchDto;
import org.jwebppy.platform.mgmt.logging.entity.DataAccessLogEntity;
import org.jwebppy.platform.mgmt.logging.entity.DataAccessLogParameterDetailEntity;
import org.jwebppy.platform.mgmt.logging.entity.DataAccessLogParameterEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface DataAccessLogMapper
{
	@NoLogging
	public void insertDataAccessLog(DataAccessLogEntity dataAccessLog);
	@NoLogging
	public void insertDataAccessLogParameter(DataAccessLogParameterEntity dataAccessLogParameter);
	@NoLogging
	public void insertDataAccessLogParameterDetail(DataAccessLogParameterDetailEntity dataAccessLogParameterDetail);
	public List<DataAccessLogEntity> findPageableLogs(DataAccessLogSearchDto dataAccessLogSearcg);
	public DataAccessLogEntity findLog(Long dlSeq);
}
