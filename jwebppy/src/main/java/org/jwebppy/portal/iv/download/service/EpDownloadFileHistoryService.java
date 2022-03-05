package org.jwebppy.portal.iv.download.service;

import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.portal.iv.common.service.IvGeneralService;
import org.jwebppy.portal.iv.download.dto.EpDownloadFileHistoryDto;
import org.jwebppy.portal.iv.download.mapper.EpDownloadFileHistoryMapper;
import org.jwebppy.portal.iv.download.mapper.EpDownloadFileHistoryObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EpDownloadFileHistoryService extends IvGeneralService
{
	@Autowired
	private EpDownloadFileHistoryMapper downloadFileHistoryMapper;

	public int create(EpDownloadFileHistoryDto downloadFileHistory)
	{
		return downloadFileHistoryMapper.insert(CmModelMapperUtils.mapToEntity(EpDownloadFileHistoryObjectMapper.INSTANCE, downloadFileHistory));
	}
}
