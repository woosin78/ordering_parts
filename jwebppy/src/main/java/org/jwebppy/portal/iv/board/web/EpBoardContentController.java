package org.jwebppy.portal.iv.board.web;

import java.io.IOException;
import java.util.List;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.util.CmNumberUtils;
import org.jwebppy.portal.iv.board.dto.EpBoardContentDto;
import org.jwebppy.portal.iv.board.dto.EpBoardContentSearchDto;
import org.jwebppy.portal.iv.board.dto.EpBoardDto;
import org.jwebppy.portal.iv.board.service.EpBoardContentService;
import org.jwebppy.portal.iv.board.service.EpBoardService;
import org.jwebppy.portal.iv.common.IvCommonVo;
import org.jwebppy.portal.iv.common.web.IvGeneralController;
import org.jwebppy.portal.iv.upload.service.EpUploadFileListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(IvCommonVo.REQUEST_PATH  + "/board")
public class EpBoardContentController extends IvGeneralController
{
	@Autowired
	private EpBoardService boardService;

	@Autowired
	private EpBoardContentService boardContentService;

	@Autowired
	private EpUploadFileListService uploadFileListService;

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
		return boardContentService.getBoardContents(boardContentSearch);
	}

	@RequestMapping("/view")
	public String view(Model model, WebRequest webRequest)
	{
		boardContentService.plusViews(webRequest.getParameter("bcSeq"));

		EpBoardContentDto boardContent = boardContentService.getBoardContent(webRequest.getParameter("bcSeq"));
		EpBoardDto board = boardContent.getBoard();

		if (board.getUfSeq() != null)
		{
			boardContent.setUploadFileLists(uploadFileListService.getUploadFileLists(board.getUfSeq(), boardContent.getBcSeq()));
		}

		model.addAttribute("boardContent", boardContent);

		setDefaultAttribute(model, webRequest);

		return DEFAULT_VIEW_URL;
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
	public Object save(@ModelAttribute EpBoardContentDto boardContent, WebRequest webRequest) throws IOException
	{
		return boardContentService.save(boardContent);
	}

	@PostMapping("/delete")
	@ResponseBody
	public Object delete(@RequestParam("bcSeq") List<String> bcSeqs)
	{
		return boardContentService.delete(bcSeqs);
	}

	@Override
	protected void setDefaultAttribute(Model model, WebRequest webRequest)
	{
		EpBoardContentSearchDto boardContentSearch = EpBoardContentSearchDto.builder()
				.bSeq(webRequest.getParameter("bSeq"))
				.bcSeq(webRequest.getParameter("bcSeq"))
				.title(webRequest.getParameter("title"))
				.writer(webRequest.getParameter("writer"))
				.fromRegDate(webRequest.getParameter("fromRegDate"))
				.toRegDate(webRequest.getParameter("toRegDate"))
				.fromView(webRequest.getParameter("fromView"))
				.toView(webRequest.getParameter("toView"))
				.pageNumber(CmNumberUtils.toInt(webRequest.getParameter("pageNumber"), 1))
				.rowPerPage(CmNumberUtils.toInt(webRequest.getParameter("rowPerPage"), PlatformCommonVo.DEFAULT_ROW_PER_PAGE))
				.build();

		model.addAttribute("boardContentSearch", boardContentSearch);
		model.addAttribute("board", boardService.getBoard(webRequest.getParameter("bSeq")));

		addAllAttributeFromRequest(model, webRequest);
	}
}
