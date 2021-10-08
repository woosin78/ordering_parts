package org.jwebppy.platform.mgmt.i18n.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.platform.mgmt.i18n.dto.LangDetailDto;
import org.jwebppy.platform.mgmt.i18n.entity.LangDetailEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LangDetailObjectMapper extends GeneralObjectMapper<LangDetailDto, LangDetailEntity>
{
	public LangDetailObjectMapper INSTANCE = Mappers.getMapper(LangDetailObjectMapper.class);
}
