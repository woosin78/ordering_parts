package org.jwebppy.platform.mgmt.upload_file.mapper;

import java.util.List;

import org.jwebppy.platform.mgmt.upload_file.dto.UploadFileListDto;
import org.jwebppy.platform.mgmt.upload_file.entity.UploadFileListEntity;

public interface UploadFileListMapper
{
	public int insert(UploadFileListEntity uploadFileList);
	public int delete(UploadFileListEntity UploadFileList);
	public List<UploadFileListEntity> findUploadFileLists(UploadFileListDto uploadFileList);
}
