package org.jwebppy.portal.iv.upload.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.portal.iv.upload.dto.EpUploadFileListDto;
import org.jwebppy.portal.iv.upload.entity.EpUploadFileListEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EpUploadFileListObjectMapper extends GeneralObjectMapper<EpUploadFileListDto, EpUploadFileListEntity>
{
	public EpUploadFileListObjectMapper INSTANCE = Mappers.getMapper(EpUploadFileListObjectMapper.class);
}
