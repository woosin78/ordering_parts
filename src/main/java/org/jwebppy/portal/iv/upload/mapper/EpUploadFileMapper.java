package org.jwebppy.portal.iv.upload.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.portal.iv.upload.entity.EpUploadFileEntity;

@NoLogging
@Mapper
public interface EpUploadFileMapper
{
	public int insert(EpUploadFileEntity uploadFile);
	public int delete(String uflSeq);
	public EpUploadFileEntity findUploadFile(String ufSeq);
	public EpUploadFileEntity findUploadFileByName(String name);
}
