package org.jwebppy.platform.mgmt.board.mapper;

import org.jwebppy.platform.mgmt.board.dto.BoardContentDto;
import org.jwebppy.platform.mgmt.board.entity.BoardContentEntity;

public interface BoardContentMapper
{
	public int insert(BoardContentEntity boardContent);
	public BoardContentEntity findBoardContent(BoardContentDto boardContent);
}
