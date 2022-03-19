package org.jwebppy.portal.iv.board.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.portal.iv.board.dto.EpBoardContentTargetDto;
import org.jwebppy.portal.iv.board.entity.EpBoardContentTargetEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EpBoardContentTargetObjectMapper extends GeneralObjectMapper<EpBoardContentTargetDto, EpBoardContentTargetEntity>
{
	public EpBoardContentTargetObjectMapper INSTANCE = Mappers.getMapper(EpBoardContentTargetObjectMapper.class);
}
