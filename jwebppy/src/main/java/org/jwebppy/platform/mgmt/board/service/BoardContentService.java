package org.jwebppy.platform.mgmt.board.service;

import java.io.IOException;
import java.util.List;

import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.MgmtGeneralService;
import org.jwebppy.platform.mgmt.board.dto.BoardContentDto;
import org.jwebppy.platform.mgmt.board.dto.BoardContentSearchDto;
import org.jwebppy.platform.mgmt.board.dto.BoardDto;
import org.jwebppy.platform.mgmt.board.entity.BoardContentEntity;
import org.jwebppy.platform.mgmt.board.mapper.BoardContentMapper;
import org.jwebppy.platform.mgmt.upload_file.service.UploadFileListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardContentService extends MgmtGeneralService
{
	@Autowired
	private BoardContentMapper boardContentMapper;

	@Autowired
	private BoardService boardService;

	@Autowired
	private UploadFileListService uploadFileListService;

	@Transactional
	public int save(BoardContentDto boardContent) throws IOException
	{
		if (boardContent.getBcSeq() == null)
		{
			return create(boardContent);
		}
		else
		{
			return modify(boardContent);
		}
	}

	public int create(BoardContentDto boardContent) throws IOException
	{
		BoardDto board = boardService.findBoard(boardContent.getBSeq());

		if (board == null)
		{
			return -1;
		}

		boardContent.setUSeq(UserAuthenticationUtils.getUserDetails().getUSeq());
		boardContent.setRegName(UserAuthenticationUtils.getUserDetails().getName());

		BoardContentEntity boardContentEntity = CmModelMapperUtils.map(boardContent, BoardContentEntity.class);

		boardContentMapper.insert(boardContentEntity);

		int bcSeq = boardContentEntity.getBcSeq();

		uploadFileListService.save(board.getUfSeq(), bcSeq, boardContent.getFiles());

		return bcSeq;
	}

	public int modify(BoardContentDto boardContent) throws IOException
	{
		if (boardContentMapper.update(CmModelMapperUtils.map(boardContent, BoardContentEntity.class)) > 0)
		{
			BoardDto board = boardService.findBoard(boardContent.getBSeq());

			uploadFileListService.save(board.getUfSeq(), boardContent.getBcSeq(), boardContent.getFiles());

			/*
			String rUflSeq = CmStringUtils.trimToEmpty(boardContent.getRUflSeq());
			String[] rUflSeqs = CmStringUtils.split(rUflSeq, PlatformCommonVo.DELIMITER);

			CmNumberUtils.

			for (String uflSeq: rUflSeqs)
			{
				if (CmStringUtils.isNotEmpty(uflSeq))
				{
					uploadFileListService.delete(Integer.parseInt(uflSeq));
				}
			}
			*/

			uploadFileListService.delete(boardContent.getUflSeqs());
		}

		return 1;
	}

	public int delete(int bcSeq)
	{
		BoardContentEntity boardContent = new BoardContentEntity();
		boardContent.setBcSeq(bcSeq);

		return boardContentMapper.delete(boardContent);
	}

	public BoardContentDto getBoardContent(int bcSeq)
	{
		return CmModelMapperUtils.map(boardContentMapper.findBoardContent(bcSeq), BoardContentDto.class);
	}

	public List<BoardContentDto> getPageableBoardContents(BoardContentSearchDto boardContentSearch)
	{
		return CmModelMapperUtils.mapAll(boardContentMapper.findPageableBoardContents(boardContentSearch), BoardContentDto.class);
	}
}
