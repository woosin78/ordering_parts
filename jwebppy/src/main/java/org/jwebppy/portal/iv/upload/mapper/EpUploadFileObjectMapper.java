package org.jwebppy.portal.iv.upload.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.portal.iv.upload.dto.EpUploadFileDto;
import org.jwebppy.portal.iv.upload.entity.EpUploadFileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EpUploadFileObjectMapper extends GeneralObjectMapper<EpUploadFileDto, EpUploadFileEntity>
{
	public EpUploadFileObjectMapper INSTANCE = Mappers.getMapper(EpUploadFileObjectMapper.class);
}
