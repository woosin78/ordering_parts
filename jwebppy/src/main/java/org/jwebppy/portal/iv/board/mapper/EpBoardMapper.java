package org.jwebppy.portal.iv.board.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.portal.iv.board.dto.EpBoardDto;
import org.jwebppy.portal.iv.board.entity.EpBoardEntity;

@NoLogging
@Mapper
public interface EpBoardMapper
{
	public EpBoardEntity findBoard(EpBoardDto board);
}
