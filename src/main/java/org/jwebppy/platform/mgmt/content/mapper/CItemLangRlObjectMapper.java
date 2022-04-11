package org.jwebppy.platform.mgmt.content.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.platform.mgmt.content.dto.CItemLangRlDto;
import org.jwebppy.platform.mgmt.content.entity.CItemLangRlEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CItemLangRlObjectMapper extends GeneralObjectMapper<CItemLangRlDto, CItemLangRlEntity>
{
	public CItemLangRlObjectMapper INSTANCE = Mappers.getMapper(CItemLangRlObjectMapper.class);
}
