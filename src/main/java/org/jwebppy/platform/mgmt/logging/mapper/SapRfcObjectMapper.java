package org.jwebppy.platform.mgmt.logging.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.platform.mgmt.logging.dto.SapRfcDto;
import org.jwebppy.platform.mgmt.logging.entity.SapRfcEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SapRfcObjectMapper extends GeneralObjectMapper<SapRfcDto, SapRfcEntity>
{
	public SapRfcObjectMapper INSTANCE = Mappers.getMapper(SapRfcObjectMapper.class);
}
