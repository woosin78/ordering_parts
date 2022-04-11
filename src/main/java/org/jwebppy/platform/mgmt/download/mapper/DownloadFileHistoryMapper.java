package org.jwebppy.platform.mgmt.download.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.platform.mgmt.download.entity.DownloadFileHistoryEntity;

@NoLogging
@Mapper
public interface DownloadFileHistoryMapper
{
	public int insert(DownloadFileHistoryEntity downloadFileHistory);
}
