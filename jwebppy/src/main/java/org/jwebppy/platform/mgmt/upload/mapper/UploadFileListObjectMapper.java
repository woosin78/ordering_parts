package org.jwebppy.platform.mgmt.upload.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.platform.mgmt.upload.dto.UploadFileListDto;
import org.jwebppy.platform.mgmt.upload.entity.UploadFileListEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UploadFileListObjectMapper extends GeneralObjectMapper<UploadFileListDto, UploadFileListEntity>
{
	public UploadFileListObjectMapper INSTANCE = Mappers.getMapper(UploadFileListObjectMapper.class);
}
