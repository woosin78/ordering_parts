package org.jwebppy.portal.iv.board.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.jwebppy.portal.iv.board.dto.EpBoardContentTargetDto;
import org.jwebppy.portal.iv.board.entity.EpBoardContentTargetEntity;

@Mapper
public interface EpBoardContentTargetMapper
{
	public int insert(EpBoardContentTargetEntity boardContentTarget);
	public int updateFgDelete(EpBoardContentTargetEntity boardContentTarget);
	public EpBoardContentTargetEntity findBoardContentTarget(EpBoardContentTargetDto boardContentTarget);
}
