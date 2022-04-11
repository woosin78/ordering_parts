package org.jwebppy.platform.mgmt.board.mapper;

import org.jwebppy.platform.core.mapper.GeneralObjectMapper;
import org.jwebppy.platform.mgmt.board.dto.BoardDto;
import org.jwebppy.platform.mgmt.board.entity.BoardEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BoardObjectMapper extends GeneralObjectMapper<BoardDto, BoardEntity>
{
	public BoardObjectMapper INSTANCE = Mappers.getMapper(BoardObjectMapper.class);
}
