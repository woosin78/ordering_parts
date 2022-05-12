package org.jwebppy.portal.iv.eu.common.file_upload.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.portal.iv.eu.common.file_upload.entity.FileDownloadHistoryEntity;

@Mapper
public interface EuFileDownloadHistoryMapper
{	
	public int insertFileDownloadHistory(FileDownloadHistoryEntity fileDownloadHistoryEntity);
}
