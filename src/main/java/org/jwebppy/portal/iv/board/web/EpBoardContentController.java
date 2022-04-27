package org.jwebppy.portal.iv.board.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.dao.support.ErpDataMap;
import org.jwebppy.platform.core.util.CmNumberUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.Formatter;
import org.jwebppy.platform.core.util.UidGenerateUtils;
import org.jwebppy.platform.mgmt.i18n.resource.I18nMessageSource;
import org.jwebppy.portal.iv.board.dto.EpBoardContentDto;
import org.jwebppy.portal.iv.board.dto.EpBoardContentSearchDto;
import org.jwebppy.portal.iv.board.dto.EpBoardContentTargetDto;
import org.jwebppy.portal.iv.board.dto.EpBoardDto;
import org.jwebppy.portal.iv.board.service.EpBoardContentService;
import org.jwebppy.portal.iv.board.service.EpBoardService;
import org.jwebppy.portal.iv.common.IvCommonVo;
import org.jwebppy.portal.iv.common.web.IvGeneralController;
import org.jwebppy.portal.iv.upload.dto.EpUploadFileDto;
import org.jwebppy.portal.iv.upload.service.EpUploadFileListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Controller
@RequestMapping(IvCommonVo.REQUEST_PATH  + "/board")
public class EpBoardContentController extends IvGeneralController
{
	@Autowired
	private EpBoardService boardService;

	@Autowired
	private EpBoardContentService boardContentService;

	@Autowired
	private I18nMessageSource I18nMessageSource;

	@Autowired
	private EpUploadFileListService uploadFileListService;

	public static void main(String[] args)
	{
		System.err.println(UidGenerateUtils.generate());
	}

	@RequestMapping("/list")
	public String list(Model model, WebRequest webRequest, EpBoardContentSearchDto boardContentSearch)
	{
		setDefaultAttribute(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/list/data")
	@ResponseBody
	public Object listData(@ModelAttribute EpBoardContentSearchDto boardContentSearch, WebRequest webRequest)
	{
		boardContentSearch.setCorp(getCorp());

		return ListUtils.emptyIfNull(boardContentService.getBoardContents(boardContentSearch));
	}

	@RequestMapping("/view")
	public String view(Model model, WebRequest webRequest)
	{
		viewProc(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/popup/view")
	public Object viewPopup(Model model, WebRequest webRequest)
	{
		viewProc(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	protected void viewProc(Model model, WebRequest webRequest)
	{
		String bcSeq = webRequest.getParameter("bcSeq");

		boardContentService.plusViews(bcSeq);

		EpBoardContentDto boardContent = boardContentService.getBoardContent(bcSeq);
		EpBoardDto board = boardContent.getBoard();
		EpUploadFileDto uploadFile = board.getUploadFile();

		if (uploadFile != null)
		{
			boardContent.setUploadFileLists(uploadFileListService.getUploadFileLists(uploadFile.getUfSeq(), boardContent.getBcSeq()));
		}

		model.addAttribute("boardContent", boardContent);

		setDefaultAttribute(model, webRequest);
	}

	@RequestMapping("/write")
	public String write(Model model, WebRequest webRequest, EpBoardContentSearchDto boardContentSearch)
	{
		model.addAttribute("boardContent", boardContentService.getBoardContent(boardContentSearch.getBcSeq()));

		setDefaultAttribute(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@PostMapping("/save")
	@ResponseBody
	public Object save(@ModelAttribute EpBoardContentDto boardContent, @RequestParam(name = "targetCode", required = false) String[] targetCodes, @RequestParam(name = "targetDescription", required = false) String[] targetDescriptions)
	{
		if (ArrayUtils.isNotEmpty(targetCodes))
		{
			List<EpBoardContentTargetDto> boardContentTargets = new ArrayList<>();
			int index = 0;

			for (String targetCode: targetCodes)
			{
				EpBoardContentTargetDto epBoardContentTarget = new EpBoardContentTargetDto();
				epBoardContentTarget.setBcSeq(boardContent.getBcSeq());
				epBoardContentTarget.setCode(targetCode);
				epBoardContentTarget.setDescription(targetDescriptions[index]);
				epBoardContentTarget.setType("D");//D:Dealer

				boardContentTargets.add(epBoardContentTarget);

				index++;
			}

			boardContent.setBoardContentTargets(boardContentTargets);
		}

		try
		{
			return boardContentService.save(boardContent);
		}
		catch (MaxUploadSizeExceededException e)
		{
			EpBoardDto board = boardService.getBoard(boardContent.getBcSeq());

			if (board.getUploadFile() != null)
			{
				return I18nMessageSource.getMessage("HQP_M_EXCEED_MAXIMUM_UPLOAD_SIZE", new String[] { Formatter.getDisplayFileSize(board.getUploadFile().getMaxFileSize()) } );
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	@PostMapping("/delete")
	@ResponseBody
	public Object delete(@RequestParam("bcSeq") List<String> bcSeqs)
	{
		return boardContentService.delete(bcSeqs);
	}

	@GetMapping("/target/data")
	@ResponseBody
	public Object targetData(@RequestParam("name") String name, @RequestParam("dealerCode") String dealerCode)
	{
		ErpDataMap rfcParamMap = getErpUserInfoByUsername();

		rfcParamMap.add(new Object[][] {
			{"name", name},
			{"dealerCode", dealerCode}
		});

		return boardContentService.getDealers(rfcParamMap);
	}

	@Override
	protected void setDefaultAttribute(Model model, WebRequest webRequest)
	{
		String bSeq = webRequest.getParameter("bSeq");

		EpBoardContentSearchDto boardContentSearch = new EpBoardContentSearchDto();
		boardContentSearch.setBSeq(bSeq);
		boardContentSearch.setBcSeq(webRequest.getParameter("bcSeq"));
		boardContentSearch.setTitle(webRequest.getParameter("title"));
		boardContentSearch.setWriter(webRequest.getParameter("writer"));
		boardContentSearch.setFromRegDate(webRequest.getParameter("fromRegDate"));
		boardContentSearch.setToRegDate(webRequest.getParameter("toRegDate"));
		boardContentSearch.setFromView(webRequest.getParameter("fromView"));
		boardContentSearch.setToView(webRequest.getParameter("toView"));
		boardContentSearch.setPageNumber(CmNumberUtils.toInt(webRequest.getParameter("pageNumber"), 1));
		boardContentSearch.setRowPerPage(CmNumberUtils.toInt(webRequest.getParameter("rowPerPage"), PlatformCommonVo.DEFAULT_ROW_PER_PAGE));

		model.addAttribute("boardContentSearch", boardContentSearch);
		model.addAttribute("board", boardService.getBoard(webRequest.getParameter("bSeq")));
		model.addAttribute("boardName", I18nMessageSource.getMessage(CmStringUtils.upperCase("PLTF_T_" + bSeq)));
		model.addAttribute("isManager", isManager());
		model.addAttribute("fgFrom", webRequest.getParameter("fgFrom"));//M:main

		addAllAttributeFromRequest(model, webRequest);
	}
}
