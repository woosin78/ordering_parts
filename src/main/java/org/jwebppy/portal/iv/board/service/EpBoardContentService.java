package org.jwebppy.portal.iv.board.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.dao.sap.RfcRequest;
import org.jwebppy.platform.core.dao.sap.RfcResponse;
import org.jwebppy.platform.core.dao.support.DataList;
import org.jwebppy.platform.core.dao.support.DataMap;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UidGenerateUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.core.util.XssHandleUtils;
import org.jwebppy.portal.iv.board.dto.EpBoardContentDto;
import org.jwebppy.portal.iv.board.dto.EpBoardContentSearchDto;
import org.jwebppy.portal.iv.board.dto.EpBoardContentTargetDto;
import org.jwebppy.portal.iv.board.dto.EpBoardDto;
import org.jwebppy.portal.iv.board.entity.EpBoardContentEntity;
import org.jwebppy.portal.iv.board.mapper.EpBoardContentMapper;
import org.jwebppy.portal.iv.board.mapper.EpBoardContentObjectMapper;
import org.jwebppy.portal.iv.common.IvCommonVo;
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
	private EpBoardContentTargetService boardContentTargetService;

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

		String bcSeq = System.currentTimeMillis() + "-" + UidGenerateUtils.generate();
		String pSeq = CmStringUtils.defaultIfEmpty(boardContent.getPSeq(), bcSeq);

		boardContent.setBcSeq(bcSeq);
		boardContent.setPSeq(pSeq);
		boardContent.setTopSeq(pSeq);
		boardContent.setUSeq(UserAuthenticationUtils.getUserDetails().getUSeq());
		boardContent.setWriter(UserAuthenticationUtils.getUserDetails().getName());
		boardContent.setHtmlContent(XssHandleUtils.removeInvalidTagAndEvent(boardContent.getHtmlContent()));

		EpBoardContentEntity boardContentEntity = CmModelMapperUtils.mapToEntity(EpBoardContentObjectMapper.INSTANCE, boardContent);

		boardContentMapper.insert(boardContentEntity);

		if (CmStringUtils.equals(board.getFgUseReply(), PlatformCommonVo.YES))
		{
			if (CmStringUtils.notEquals(pSeq, bcSeq))
			{
				boardContentMapper.updatePlusSort(boardContentEntity);

				EpBoardContentDto pBoardContent = getBoardContent(pSeq);

				boardContentEntity.setSort(pBoardContent.getSort());
				boardContentEntity.setDepth(pBoardContent.getDepth()+1);
				boardContentEntity.setTopSeq(pBoardContent.getTopSeq());

				boardContentMapper.updateForReply(boardContentEntity);
			}
		}

		uploadFileListService.save(board.getUploadFile(), bcSeq, boardContent.getFiles());

		if (CmStringUtils.equals(board.getFgSetTarget(), IvCommonVo.YES))
		{
			List<EpBoardContentTargetDto> boardContentTargets = boardContent.getBoardContentTargets();

			if (CollectionUtils.isNotEmpty(boardContentTargets))
			{
				for (EpBoardContentTargetDto boardContentTarget: boardContentTargets)
				{
					boardContentTarget.setBcSeq(bcSeq);
				}
			}

			boardContentTargetService.save(boardContent.getBoardContentTargets());
		}

		return bcSeq;
	}

	public String modify(EpBoardContentDto boardContent) throws IOException
	{
		boardContent.setHtmlContent(XssHandleUtils.removeInvalidTagAndEvent(boardContent.getHtmlContent()));

		if (boardContentMapper.update(CmModelMapperUtils.mapToEntity(EpBoardContentObjectMapper.INSTANCE, boardContent)) > 0)
		{
			EpBoardDto board = boardService.getBoard(boardContent.getBSeq());

			String bcSeq = boardContent.getBcSeq();

			uploadFileListService.save(board.getUploadFile(), bcSeq, boardContent.getFiles());

			uploadFileListService.delete(boardContent.getUflSeqs());

			if (CmStringUtils.equals(board.getFgSetTarget(), IvCommonVo.YES))
			{
				boardContentTargetService.delete(bcSeq);

				boardContentTargetService.save(boardContent.getBoardContentTargets());
			}
		}

		return boardContent.getBcSeq();
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

	public List<EpBoardContentDto> getBoardContents(EpBoardContentSearchDto boardContentSearch)
	{
		return CmModelMapperUtils.mapToDto(EpBoardContentObjectMapper.INSTANCE, boardContentMapper.findBoardContents(boardContentSearch));
	}

	public List<DataMap> getDealers(ErpDataMap paramMap)
	{
		RfcRequest rfcRequest = new RfcRequest("Z_SD_DEALER_LIST");

		rfcRequest
			.field()
				.add(new Object[][] {
					{"I_VTWEG", paramMap.getDistChannel()},
					{"I_SPART", paramMap.getDivision()}
				})
			.and()
			.table("T_VKORG")
				.add("VKORG", paramMap.getSalesOrg())
			.output("T_LIST");

		RfcResponse rfcResponse = simpleRfcTemplate.response(rfcRequest);

		DataList dataList = rfcResponse.getTable("T_LIST");

		if (CollectionUtils.isNotEmpty(dataList))
		{
			String name = paramMap.getString("name");
			String dealerCode = paramMap.getString("dealerCode");

			List<DataMap> resultList = new ArrayList<>();

			for (int i=0, size=dataList.size(); i<size; i++)
			{
				DataMap dataMap = (DataMap)dataList.get(i);

				if (CmStringUtils.indexOfIgnoreCaseAndEmpty(dataMap.getString("NAME1"), name) > -1 || CmStringUtils.indexOfIgnoreCaseAndEmpty(dataMap.getString("KUNNR"), dealerCode) > -1)
				{
					resultList.add(dataMap);
				}
			}

			return resultList;
		}

		return Collections.emptyList();
	}
}
