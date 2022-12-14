package org.jwebppy.platform.mgmt.upload.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.platform.mgmt.upload.dto.UploadFileListDto;
import org.jwebppy.platform.mgmt.upload.entity.UploadFileListEntity;

@NoLogging
@Mapper
public interface UploadFileListMapper
{
	public int insert(UploadFileListEntity uploadFileList);
	public int delete(UploadFileListEntity UploadFileList);
	public UploadFileListEntity findUploadFileList(String uflSeq);
	public List<UploadFileListEntity> findUploadFileLists(UploadFileListDto uploadFileList);
}
