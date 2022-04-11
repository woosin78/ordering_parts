package org.jwebppy.portal.iv.download.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.portal.iv.download.dto.EpDownloadFileHistoryDto;
import org.jwebppy.portal.iv.download.entity.EpDownloadFileHistoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EpDownloadFileHistoryObjectMapper extends GeneralObjectMapper<EpDownloadFileHistoryDto, EpDownloadFileHistoryEntity>
{
	public EpDownloadFileHistoryObjectMapper INSTANCE = Mappers.getMapper(EpDownloadFileHistoryObjectMapper.class);
}
