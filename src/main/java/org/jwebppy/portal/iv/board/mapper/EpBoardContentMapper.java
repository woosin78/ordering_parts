package org.jwebppy.portal.iv.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.portal.iv.board.dto.EpBoardContentSearchDto;
import org.jwebppy.portal.iv.board.entity.EpBoardContentEntity;

//@NoLogging
@Mapper
public interface EpBoardContentMapper
{
	public int insert(EpBoardContentEntity boardContent);
	public int update(EpBoardContentEntity boardContent);
	public int updatePSeq(EpBoardContentEntity boardContent);
	public int updatePlusViews(String bcSeq);
	public int updateForReply(String pSeq);
	public int delete(EpBoardContentEntity boardContent);
	public EpBoardContentEntity findBoardContent(String bcSeq);
	public List<EpBoardContentEntity> findBoardContents(EpBoardContentSearchDto boardContentSearch);
}