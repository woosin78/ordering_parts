package org.jwebppy.platform.mgmt.i18n.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.platform.mgmt.i18n.dto.LangKindDto;
import org.jwebppy.platform.mgmt.i18n.entity.LangKindEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LangKindObjectMapper extends GeneralObjectMapper<LangKindDto, LangKindEntity>
{
	public LangKindObjectMapper INSTANCE = Mappers.getMapper(LangKindObjectMapper.class);
}
