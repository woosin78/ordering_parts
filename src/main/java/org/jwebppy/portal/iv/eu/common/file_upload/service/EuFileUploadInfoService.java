package org.jwebppy.portal.iv.eu.common.file_upload.service;

import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.portal.iv.eu.common.file_upload.dto.FileUploadInfoDto;
import org.jwebppy.portal.iv.eu.common.file_upload.mapper.EuFileUploadInfoMapper;
import org.jwebppy.portal.iv.eu.common.service.EuGeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EuFileUploadInfoService extends EuGeneralService
{
	@Autowired
	private EuFileUploadInfoMapper fileUploadInfoMapper;

	public FileUploadInfoDto getFileUploadInfo(String fuiId)
	{
		return CmModelMapperUtils.map(fileUploadInfoMapper.findFileUploadInfo(fuiId), FileUploadInfoDto.class);
	}

}



