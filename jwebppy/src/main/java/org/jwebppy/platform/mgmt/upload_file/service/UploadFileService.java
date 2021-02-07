package org.jwebppy.platform.mgmt.upload_file.service;

import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.mgmt.MgmtGeneralService;
import org.jwebppy.platform.mgmt.upload_file.dto.UploadFileDto;
import org.jwebppy.platform.mgmt.upload_file.entity.UploadFileEntity;
import org.jwebppy.platform.mgmt.upload_file.mapper.UploadFileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UploadFileService extends MgmtGeneralService
{
	@Autowired
	private UploadFileMapper uploadFileMapper;

	public int insert(UploadFileEntity uploadFile)
	{
		return uploadFileMapper.insert(uploadFile);
	}

	public UploadFileDto findUploadFile(Integer ufSeq)
	{
		return CmModelMapperUtils.map(uploadFileMapper.findUploadFile(ufSeq), UploadFileDto.class);
	}
}
