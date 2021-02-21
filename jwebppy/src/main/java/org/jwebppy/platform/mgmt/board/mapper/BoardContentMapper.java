package org.jwebppy.platform.mgmt.board.mapper;

import java.util.List;

import org.jwebppy.platform.mgmt.board.dto.BoardContentSearchDto;
import org.jwebppy.platform.mgmt.board.entity.BoardContentEntity;

public interface BoardContentMapper
{
	public int insert(BoardContentEntity boardContent);
	public int update(BoardContentEntity boardContent);
	public int updatePSeq(BoardContentEntity boardContent);
	public int updatePlusViews(Integer bcSeq);
	public int updateForReply(Integer pSeq);
	public int delete(BoardContentEntity boardContent);
	public BoardContentEntity findBoardContent(Integer bcSeq);
	public List<BoardContentEntity> findPageableBoardContents(BoardContentSearchDto boardContentSearch);
}
