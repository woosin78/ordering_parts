package org.jwebppy.portal.iv.board.service;

import org.jwebppy.platform.common.service.PlatformGeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.portal.iv.board.dto.EpBoardDto;
import org.jwebppy.portal.iv.board.mapper.EpBoardMapper;
import org.jwebppy.portal.iv.board.mapper.EpBoardObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EpBoardService extends PlatformGeneralService
{
	@Autowired
	private EpBoardMapper boardMapper;

	public EpBoardDto getBoard(String bSeq)
	{
		EpBoardDto board = EpBoardDto.builder()
				.bSeq(bSeq)
				.build();

		return getBoard(board);
	}

	public EpBoardDto getBoard(EpBoardDto board)
	{
		return CmModelMapperUtils.mapToDto(EpBoardObjectMapper.INSTANCE, boardMapper.findBoard(board));
	}
}
