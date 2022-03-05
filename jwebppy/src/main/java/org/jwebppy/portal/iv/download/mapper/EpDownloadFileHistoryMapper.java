package org.jwebppy.portal.iv.download.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.portal.iv.download.entity.EpDownloadFileHistoryEntity;

@NoLogging
@Mapper
public interface EpDownloadFileHistoryMapper
{
	public int insert(EpDownloadFileHistoryEntity downloadFileHistory);
}
