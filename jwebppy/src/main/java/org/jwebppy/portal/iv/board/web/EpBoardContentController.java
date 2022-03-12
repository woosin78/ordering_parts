package org.jwebppy.portal.iv.board.web;

import java.io.IOException;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
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
		boardContentSearch.setCorp(getCorp());

		return ListUtils.emptyIfNull(boardContentService.getBoardContents(boardContentSearch));
	}

	@RequestMapping("/view")
	public String view(Model model, WebRequest webRequest)
	{
		String bcSeq = webRequest.getParameter("bcSeq");

		boardContentService.plusViews(bcSeq);

		EpBoardContentDto boardContent = boardContentService.getBoardContent(bcSeq);
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
		EpBoardContentSearchDto boardContentSearch = new EpBoardContentSearchDto();
		boardContentSearch.setBSeq(webRequest.getParameter("bSeq"));
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

		addAllAttributeFromRequest(model, webRequest);
	}
}
