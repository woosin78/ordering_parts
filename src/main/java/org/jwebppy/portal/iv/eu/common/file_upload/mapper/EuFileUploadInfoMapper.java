package org.jwebppy.portal.iv.eu.common.file_upload.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.portal.iv.eu.common.file_upload.entity.FileUploadInfoEntity;

@Mapper
public interface EuFileUploadInfoMapper
{
	public FileUploadInfoEntity findFileUploadInfo(String fuiId);
}