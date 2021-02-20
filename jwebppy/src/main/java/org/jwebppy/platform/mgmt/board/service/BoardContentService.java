package org.jwebppy.platform.mgmt.board.service;

import java.io.IOException;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.MgmtGeneralService;
import org.jwebppy.platform.mgmt.board.dto.BoardContentDto;
import org.jwebppy.platform.mgmt.board.dto.BoardContentSearchDto;
import org.jwebppy.platform.mgmt.board.dto.BoardDto;
import org.jwebppy.platform.mgmt.board.entity.BoardContentEntity;
import org.jwebppy.platform.mgmt.board.mapper.BoardContentMapper;
import org.jwebppy.platform.mgmt.upload.service.UploadFileListService;
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

	@Transactional
	public int create(BoardContentDto boardContent) throws IOException
	{
		BoardDto board = boardService.getBoard(boardContent.getBSeq());

		if (board == null)
		{
			return -1;
		}

		boardContent.setUSeq(UserAuthenticationUtils.getUserDetails().getUSeq());
		boardContent.setWriter(UserAuthenticationUtils.getUserDetails().getName());

		BoardContentEntity boardContentEntity = CmModelMapperUtils.map(boardContent, BoardContentEntity.class);

		boardContentMapper.insert(boardContentEntity);

		if (CmStringUtils.equals(board.getFgUseReply(), PlatformCommonVo.YES))
		{
			if (boardContent.getPSeq() != null)
			{
				boardContentMapper.updateForReply(boardContent.getPSeq());
			}
		}

		int bcSeq = boardContentEntity.getBcSeq();

		uploadFileListService.save(board.getUfSeq(), bcSeq, boardContent.getFiles());

		return bcSeq;
	}

	@Transactional
	public int modify(BoardContentDto boardContent) throws IOException
	{
		if (boardContentMapper.update(CmModelMapperUtils.map(boardContent, BoardContentEntity.class)) > 0)
		{
			BoardDto board = boardService.getBoard(boardContent.getBSeq());

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

	public void plusViews(int bcSeq)
	{
		boardContentMapper.updatePlusViews(bcSeq);
	}

	@Transactional
	public int delete(List<Integer> bcSeqs)
	{
		if (CollectionUtils.isNotEmpty(bcSeqs))
		{
			for (Integer bcSeq: bcSeqs)
			{
				System.err.println(bcSeq);

				if (bcSeq != null)
				{
					BoardContentEntity boardContent = new BoardContentEntity();
					boardContent.setBcSeq(bcSeq);

					boardContentMapper.delete(boardContent);
				}
			}

			return 1;
		}

		return 0;
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
