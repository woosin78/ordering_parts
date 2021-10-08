package org.jwebppy.platform.mgmt.board.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.platform.mgmt.board.dto.BoardContentDto;
import org.jwebppy.platform.mgmt.board.entity.BoardContentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BoardContentObjectMapper extends GeneralObjectMapper<BoardContentDto, BoardContentEntity>
{
	public BoardContentObjectMapper INSTANCE = Mappers.getMapper(BoardContentObjectMapper.class);
}
