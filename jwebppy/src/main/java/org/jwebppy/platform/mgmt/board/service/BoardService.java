package org.jwebppy.platform.mgmt.board.service;

import org.jwebppy.platform.PlatformGeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.mgmt.board.dto.BoardDto;
import org.jwebppy.platform.mgmt.board.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService extends PlatformGeneralService
{
	@Autowired
	private BoardMapper boardMapper;

	public BoardDto findBoard(Integer bSeq)
	{
		BoardDto board = new BoardDto();
		board.setBSeq(bSeq);

		return findBoard(board);
	}

	public BoardDto findBoard(String bId)
	{
		BoardDto board = new BoardDto();
		board.setBId(bId);

		return findBoard(board);
	}

	public BoardDto findBoard(BoardDto board)
	{
		return CmModelMapperUtils.map(boardMapper.findBoard(board), BoardDto.class);
	}
}
