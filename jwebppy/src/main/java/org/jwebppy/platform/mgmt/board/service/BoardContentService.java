package org.jwebppy.platform.mgmt.board.service;

import java.io.IOException;

import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.MgmtGeneralService;
import org.jwebppy.platform.mgmt.board.dto.BoardContentDto;
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
		BoardDto board = boardService.findBoard(boardContent.getBSeq());

		System.err.println(board);

		if (board == null)
		{
			return -1;
		}

		boardContent.setUSeq(UserAuthenticationUtils.getUserDetails().getUSeq());

		int bcSeq = boardContentMapper.insert(CmModelMapperUtils.map(boardContent, BoardContentEntity.class));

		System.err.println("bcSeq:" + bcSeq);

		uploadFileListService.save(board.getUfSeq(), bcSeq, boardContent.getFiles());

		return bcSeq;
	}
}
