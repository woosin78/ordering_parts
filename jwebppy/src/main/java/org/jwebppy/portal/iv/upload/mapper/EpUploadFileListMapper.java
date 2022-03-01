package org.jwebppy.portal.iv.upload.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.portal.iv.upload.dto.EpUploadFileListDto;
import org.jwebppy.portal.iv.upload.entity.EpUploadFileListEntity;

@NoLogging
@Mapper
public interface EpUploadFileListMapper
{
	public int insert(EpUploadFileListEntity uploadFileList);
	public int delete(EpUploadFileListEntity UploadFileList);
	public EpUploadFileListEntity findUploadFileList(String uflSeq);
	public List<EpUploadFileListEntity> findUploadFileLists(EpUploadFileListDto uploadFileList);
}
