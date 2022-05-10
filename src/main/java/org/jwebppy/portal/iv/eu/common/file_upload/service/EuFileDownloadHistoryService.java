package org.jwebppy.portal.iv.eu.common.file_upload.service;

import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.portal.iv.eu.common.file_upload.dto.FileDownloadHistoryDto;
import org.jwebppy.portal.iv.eu.common.file_upload.entity.FileDownloadHistoryEntity;
import org.jwebppy.portal.iv.eu.common.file_upload.mapper.EuFileDownloadHistoryMapper;
import org.jwebppy.portal.iv.eu.common.service.EuGeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EuFileDownloadHistoryService extends EuGeneralService
{	
	@Autowired
	private EuFileDownloadHistoryMapper fileDownloadHistoryMapper;
	
	//다운로드 히스토리 등록
	public int insertFileDownloadHistory(FileDownloadHistoryDto fileDownloadHistoryDto)
	{		
		FileDownloadHistoryEntity fileDownloadHistoryEntity = CmModelMapperUtils.map(fileDownloadHistoryDto, FileDownloadHistoryEntity.class);		
		return fileDownloadHistoryMapper.insertFileDownloadHistory(fileDownloadHistoryEntity);		
	}			
}	
