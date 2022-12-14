package org.jwebppy.platform.mgmt.download.service;

import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.mgmt.common.service.MgmtGeneralService;
import org.jwebppy.platform.mgmt.download.dto.DownloadFileHistoryDto;
import org.jwebppy.platform.mgmt.download.mapper.DownloadFileHistoryMapper;
import org.jwebppy.platform.mgmt.download.mapper.DownloadFileHistoryObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DownloadFileHistoryService extends MgmtGeneralService
{
	@Autowired
	private DownloadFileHistoryMapper downloadFileHistoryMapper;

	public int create(DownloadFileHistoryDto downloadFileHistory)
	{
		return downloadFileHistoryMapper.insert(CmModelMapperUtils.mapToEntity(DownloadFileHistoryObjectMapper.INSTANCE, downloadFileHistory));
	}
}
