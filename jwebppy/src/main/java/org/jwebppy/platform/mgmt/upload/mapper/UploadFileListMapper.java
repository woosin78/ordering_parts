package org.jwebppy.platform.mgmt.upload.mapper;

import java.util.List;

import org.jwebppy.platform.mgmt.upload.dto.UploadFileListDto;
import org.jwebppy.platform.mgmt.upload.entity.UploadFileListEntity;

public interface UploadFileListMapper
{
	public int insert(UploadFileListEntity uploadFileList);
	public int delete(UploadFileListEntity UploadFileList);
	public UploadFileListEntity findUploadFileList(int uflSeq);
	public List<UploadFileListEntity> findUploadFileLists(UploadFileListDto uploadFileList);
}
