package org.jwebppy.platform.mgmt.upload.service;

import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.mgmt.common.service.MgmtGeneralService;
import org.jwebppy.platform.mgmt.upload.dto.UploadFileDto;
import org.jwebppy.platform.mgmt.upload.entity.UploadFileEntity;
import org.jwebppy.platform.mgmt.upload.mapper.UploadFileMapper;
import org.jwebppy.platform.mgmt.upload.mapper.UploadFileObjectMapper;
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

	public UploadFileDto getUploadFile(Integer ufSeq)
	{
		return CmModelMapperUtils.mapToDto(UploadFileObjectMapper.INSTANCE, uploadFileMapper.findUploadFile(ufSeq));
	}
}
