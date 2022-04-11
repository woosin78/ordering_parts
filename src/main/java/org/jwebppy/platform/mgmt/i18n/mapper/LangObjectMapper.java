package org.jwebppy.platform.mgmt.i18n.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.platform.mgmt.i18n.dto.LangDto;
import org.jwebppy.platform.mgmt.i18n.entity.LangEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LangObjectMapper extends GeneralObjectMapper<LangDto, LangEntity>
{
	public LangObjectMapper INSTANCE = Mappers.getMapper(LangObjectMapper.class);
}
