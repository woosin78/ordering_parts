package org.jwebppy.platform.mgmt.board.mapper;

import java.util.List;

import org.jwebppy.platform.mgmt.board.dto.BoardContentSearchDto;
import org.jwebppy.platform.mgmt.board.entity.BoardContentEntity;

public interface BoardContentMapper
{
	public int insert(BoardContentEntity boardContent);
	public int update(BoardContentEntity boardContent);
	public int updatePlusViews(int bcSeq);
	public int delete(BoardContentEntity boardContent);
	public BoardContentEntity findBoardContent(int bcSeq);
	public List<BoardContentEntity> findPageableBoardContents(BoardContentSearchDto boardContentSearch);
}
