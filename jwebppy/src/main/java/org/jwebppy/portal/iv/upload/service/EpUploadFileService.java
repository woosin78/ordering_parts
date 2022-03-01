package org.jwebppy.portal.iv.upload.service;

import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.portal.iv.common.service.IvGeneralService;
import org.jwebppy.portal.iv.upload.dto.EpUploadFileDto;
import org.jwebppy.portal.iv.upload.entity.EpUploadFileEntity;
import org.jwebppy.portal.iv.upload.mapper.EpUploadFileMapper;
import org.jwebppy.portal.iv.upload.mapper.EpUploadFileObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EpUploadFileService extends IvGeneralService
{
	@Autowired
	private EpUploadFileMapper uploadFileMapper;

	public int insert(EpUploadFileEntity uploadFile)
	{
		return uploadFileMapper.insert(uploadFile);
	}

	public EpUploadFileDto getUploadFile(String ufSeq)
	{
		return CmModelMapperUtils.mapToDto(EpUploadFileObjectMapper.INSTANCE, uploadFileMapper.findUploadFile(ufSeq));
	}
}
