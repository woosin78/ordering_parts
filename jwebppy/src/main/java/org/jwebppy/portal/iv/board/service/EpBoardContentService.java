package org.jwebppy.portal.iv.board.service;

import java.io.IOException;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UidGenerateUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.portal.iv.board.dto.EpBoardContentDto;
import org.jwebppy.portal.iv.board.dto.EpBoardContentSearchDto;
import org.jwebppy.portal.iv.board.dto.EpBoardDto;
import org.jwebppy.portal.iv.board.entity.EpBoardContentEntity;
import org.jwebppy.portal.iv.board.mapper.EpBoardContentMapper;
import org.jwebppy.portal.iv.board.mapper.EpBoardContentObjectMapper;
import org.jwebppy.portal.iv.common.service.IvGeneralService;
import org.jwebppy.portal.iv.upload.service.EpUploadFileListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EpBoardContentService extends IvGeneralService
{
	@Autowired
	private EpBoardContentMapper boardContentMapper;

	@Autowired
	private EpBoardService boardService;

	@Autowired
	private EpUploadFileListService uploadFileListService;

	public String save(EpBoardContentDto boardContent) throws IOException
	{
		if (CmStringUtils.isEmpty(boardContent.getBcSeq()))
		{
			return create(boardContent);
		}
		else
		{
			return modify(boardContent);
		}
	}

	public String create(EpBoardContentDto boardContent) throws IOException
	{
		EpBoardDto board = boardService.getBoard(boardContent.getBSeq());

		if (board == null)
		{
			return null;
		}

		boardContent.setBcSeq(System.currentTimeMillis() + "-" + UidGenerateUtils.generate());
		boardContent.setUSeq(UserAuthenticationUtils.getUserDetails().getUSeq());
		boardContent.setWriter(UserAuthenticationUtils.getUserDetails().getName());

		EpBoardContentEntity boardContentEntity = CmModelMapperUtils.mapToEntity(EpBoardContentObjectMapper.INSTANCE, boardContent);

		boardContentMapper.insert(boardContentEntity);

		String bcSeq = boardContentEntity.getBcSeq();
		String pSeq = boardContent.getPSeq();

		boardContentEntity.setPSeq(CmStringUtils.defaultIfEmpty(pSeq, bcSeq));
		boardContentMapper.updatePSeq(boardContentEntity);

		if (CmStringUtils.equals(board.getFgUseReply(), PlatformCommonVo.YES))
		{
			if (CmStringUtils.isNotEmpty(pSeq))
			{
				boardContentMapper.updateForReply(pSeq);
			}
		}

		uploadFileListService.save(board.getUfSeq(), bcSeq, boardContent.getFiles());

		return bcSeq;
	}

	public String modify(EpBoardContentDto boardContent) throws IOException
	{
		if (boardContentMapper.update(CmModelMapperUtils.mapToEntity(EpBoardContentObjectMapper.INSTANCE, boardContent)) > 0)
		{
			EpBoardDto board = boardService.getBoard(boardContent.getBSeq());

			uploadFileListService.save(board.getUfSeq(), boardContent.getBcSeq(), boardContent.getFiles());

			uploadFileListService.delete(boardContent.getUflSeqs());
		}

		return boardContent.getBSeq();
	}

	public void plusViews(String bcSeq)
	{
		boardContentMapper.updatePlusViews(bcSeq);
	}

	public int delete(List<String> bcSeqs)
	{
		if (CollectionUtils.isNotEmpty(bcSeqs))
		{
			for (String bcSeq: bcSeqs)
			{
				if (bcSeq != null)
				{
					EpBoardContentEntity boardContent = new EpBoardContentEntity();
					boardContent.setBcSeq(bcSeq);

					boardContentMapper.delete(boardContent);
				}
			}

			return 1;
		}

		return 0;
	}

	public EpBoardContentDto getBoardContent(String bcSeq)
	{
		return CmModelMapperUtils.mapToDto(EpBoardContentObjectMapper.INSTANCE, boardContentMapper.findBoardContent(bcSeq));
	}

	public List<EpBoardContentDto> getPageableBoardContents(EpBoardContentSearchDto boardContentSearch)
	{
		return CmModelMapperUtils.mapToDto(EpBoardContentObjectMapper.INSTANCE, boardContentMapper.findPageableBoardContents(boardContentSearch));
	}
}
