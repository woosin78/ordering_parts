package org.jwebppy.platform.mgmt.upload_file.mapper;

import org.jwebppy.platform.mgmt.upload_file.entity.UploadFileEntity;

public interface UploadFileMapper
{
	public int insert(UploadFileEntity uploadFile);
	public int delete(Integer uflSeq);
	public UploadFileEntity findUploadFile(Integer ufSeq);
}
