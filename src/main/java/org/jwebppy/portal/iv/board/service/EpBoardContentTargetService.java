package org.jwebppy.portal.iv.board.service;

import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.portal.iv.board.dto.EpBoardContentTargetDto;
import org.jwebppy.portal.iv.board.entity.EpBoardContentTargetEntity;
import org.jwebppy.portal.iv.board.mapper.EpBoardContentTargetMapper;
import org.jwebppy.portal.iv.board.mapper.EpBoardContentTargetObjectMapper;
import org.jwebppy.portal.iv.common.service.IvGeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EpBoardContentTargetService extends IvGeneralService
{
	@Autowired
	private EpBoardContentTargetMapper boardContentTargetMapper;

	public int save(EpBoardContentTargetDto boardContentTarget)
	{
		return boardContentTargetMapper.insert(CmModelMapperUtils.mapToEntity(EpBoardContentTargetObjectMapper.INSTANCE, boardContentTarget));
	}

	public int save(List<EpBoardContentTargetDto> boardContentTargets)
	{
		for (EpBoardContentTargetDto boardContentTarget: ListUtils.emptyIfNull(boardContentTargets))
		{
			save(boardContentTarget);
		}

		return 1;
	}

	public int delete(String bcSeq)
	{
		EpBoardContentTargetEntity epBoardContentTarget = new EpBoardContentTargetEntity();
		epBoardContentTarget.setBcSeq(bcSeq);

		return boardContentTargetMapper.updateFgDelete(epBoardContentTarget);
	}

	public EpBoardContentTargetDto getBoardContentTarget(EpBoardContentTargetDto boardContentTarget)
	{
		return CmModelMapperUtils.mapToDto(EpBoardContentTargetObjectMapper.INSTANCE, boardContentTargetMapper.findBoardContentTarget(boardContentTarget));
	}
}
