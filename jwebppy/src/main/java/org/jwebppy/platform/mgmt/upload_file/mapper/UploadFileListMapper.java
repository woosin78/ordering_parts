package org.jwebppy.platform.mgmt.upload_file.mapper;

import org.jwebppy.platform.mgmt.upload_file.entity.UploadFileListEntity;

public interface UploadFileListMapper
{
	public int insert(UploadFileListEntity uploadFileList);
	public int delete(Integer uflSeq);
}
