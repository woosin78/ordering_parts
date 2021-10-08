package org.jwebppy.platform.mgmt.upload.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.platform.mgmt.upload.entity.UploadFileEntity;

@NoLogging
@Mapper
public interface UploadFileMapper
{
	public int insert(UploadFileEntity uploadFile);
	public int delete(Integer uflSeq);
	public UploadFileEntity findUploadFile(Integer ufSeq);
}
