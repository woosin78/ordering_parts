package org.jwebppy.platform.mgmt.board.service;

import org.jwebppy.platform.PlatformGeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.mgmt.board.dto.BoardDto;
import org.jwebppy.platform.mgmt.board.mapper.BoardMapper;
import org.jwebppy.platform.mgmt.board.mapper.BoardObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService extends PlatformGeneralService
{
	@Autowired
	private BoardMapper boardMapper;

	public BoardDto getBoard(Integer bSeq)
	{
		BoardDto board = new BoardDto();
		board.setBSeq(bSeq);

		return getBoard(board);
	}

	public BoardDto getBoard(String bId)
	{
		BoardDto board = new BoardDto();
		board.setBId(bId);

		return getBoard(board);
	}

	public BoardDto getBoard(BoardDto board)
	{
		return CmModelMapperUtils.mapToDto(BoardObjectMapper.INSTANCE, boardMapper.findBoard(board));
	}
}
