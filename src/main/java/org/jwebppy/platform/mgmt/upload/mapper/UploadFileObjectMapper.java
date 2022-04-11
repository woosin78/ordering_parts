package org.jwebppy.platform.mgmt.upload.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.platform.mgmt.upload.dto.UploadFileDto;
import org.jwebppy.platform.mgmt.upload.entity.UploadFileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UploadFileObjectMapper extends GeneralObjectMapper<UploadFileDto, UploadFileEntity>
{
	public UploadFileObjectMapper INSTANCE = Mappers.getMapper(UploadFileObjectMapper.class);
}
