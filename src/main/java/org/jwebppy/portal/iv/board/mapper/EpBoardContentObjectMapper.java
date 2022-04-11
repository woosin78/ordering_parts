package org.jwebppy.portal.iv.board.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.portal.iv.board.dto.EpBoardContentDto;
import org.jwebppy.portal.iv.board.entity.EpBoardContentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EpBoardContentObjectMapper extends GeneralObjectMapper<EpBoardContentDto, EpBoardContentEntity>
{
	public EpBoardContentObjectMapper INSTANCE = Mappers.getMapper(EpBoardContentObjectMapper.class);
}
