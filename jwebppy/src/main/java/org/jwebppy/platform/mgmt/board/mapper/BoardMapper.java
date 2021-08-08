package org.jwebppy.platform.mgmt.board.mapper;

import org.jwebppy.platform.core.interceptor.NoLogging;
import org.jwebppy.platform.mgmt.board.dto.BoardDto;
import org.jwebppy.platform.mgmt.board.entity.BoardEntity;

@NoLogging
public interface BoardMapper
{
	public BoardEntity findBoard(BoardDto board);
}
