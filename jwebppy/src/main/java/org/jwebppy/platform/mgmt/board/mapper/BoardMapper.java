package org.jwebppy.platform.mgmt.board.mapper;

import org.jwebppy.platform.mgmt.board.dto.BoardDto;
import org.jwebppy.platform.mgmt.board.entity.BoardEntity;

public interface BoardMapper
{
	public BoardEntity findBoard(BoardDto board);
}
