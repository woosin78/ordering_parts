package org.jwebppy.platform.mgmt.download.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.platform.mgmt.download.dto.DownloadFileHistoryDto;
import org.jwebppy.platform.mgmt.download.entity.DownloadFileHistoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DownloadFileHistoryObjectMapper extends GeneralObjectMapper<DownloadFileHistoryDto, DownloadFileHistoryEntity>
{
	public DownloadFileHistoryObjectMapper INSTANCE = Mappers.getMapper(DownloadFileHistoryObjectMapper.class);
}
