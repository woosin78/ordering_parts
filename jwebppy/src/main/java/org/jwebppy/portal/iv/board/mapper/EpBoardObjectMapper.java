package org.jwebppy.portal.iv.board.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.portal.iv.board.dto.EpBoardDto;
import org.jwebppy.portal.iv.board.entity.EpBoardEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EpBoardObjectMapper extends GeneralObjectMapper<EpBoardDto, EpBoardEntity>
{
	public EpBoardObjectMapper INSTANCE = Mappers.getMapper(EpBoardObjectMapper.class);
}
