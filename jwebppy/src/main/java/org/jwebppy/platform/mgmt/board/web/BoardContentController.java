package org.jwebppy.platform.mgmt.board.web;

import java.io.IOException;
import java.util.List;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.util.CmNumberUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.web.ui.pagination.PageableList;
import org.jwebppy.platform.mgmt.MgmtGeneralController;
import org.jwebppy.platform.mgmt.board.dto.BoardContentDto;
import org.jwebppy.platform.mgmt.board.dto.BoardContentSearchDto;
import org.jwebppy.platform.mgmt.board.service.BoardContentService;
import org.jwebppy.platform.mgmt.board.service.BoardService;
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
@RequestMapping(PlatformConfigVo.CONTEXT_PATH + "/mgmt/board/content")
public class BoardContentController extends MgmtGeneralController
{
	@Autowired
	private BoardService boardService;

	@Autowired
	private BoardContentService boardContentService;

	@RequestMapping("/list")
	public String list(Model model, WebRequest webRequest)
	{
		setDefaultAttribute(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/list/data")
	@ResponseBody
	public Object listData(@ModelAttribute BoardContentSearchDto boardContentSearch)
	{
		return BoardContentLayoutBuilder.getList(new PageableList<>(boardContentService.getPageableBoardContents(boardContentSearch)));
	}

	@RequestMapping("/view")
	public String view(Model model, WebRequest webRequest)
	{
		boardContentService.plusViews(CmNumberUtils.toInt(webRequest.getParameter("bcSeq")));

		model.addAttribute("boardContent", boardContentService.getBoardContent(CmNumberUtils.toInt(webRequest.getParameter("bcSeq"), 0)));

		setDefaultAttribute(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/view/data")
	@ResponseBody
	public Object detailData(@RequestParam("bcSeq") Integer bcSeq)
	{
		return boardContentService.getBoardContent(bcSeq);
	}

	@RequestMapping("/viewer")
	public Object viewer(Model model, WebRequest webRequest)
	{
		model.addAttribute("boardContent", boardContentService.getBoardContent(CmNumberUtils.toInt(webRequest.getParameter("bcSeq"), 0)));

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/write")
	public String write(Model model, WebRequest webRequest)
	{
		setDefaultAttribute(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@PostMapping("/save")
	@ResponseBody
	public Object save(@ModelAttribute BoardContentDto boardContent) throws IOException
	{
		return boardContentService.save(boardContent);
	}

	@PostMapping("/delete")
	@ResponseBody
	public Object delete(@RequestParam("bcSeq") List<Integer> bcSeqs)
	{
		return boardContentService.delete(bcSeqs);
	}

	@Override
	protected void setDefaultAttribute(Model model, WebRequest webRequest)
	{
		model.addAttribute("board", boardService.getBoard(webRequest.getParameter("bId")));
		model.addAttribute("type", CmStringUtils.defaultString(webRequest.getParameter("type"), "TC"));
		model.addAttribute("pageNumber", CmStringUtils.defaultString(webRequest.getParameter("pageNumber"), "1"));
		model.addAttribute("rowPerPage", CmStringUtils.defaultString(webRequest.getParameter("rowPerPage"), PlatformCommonVo.DEFAULT_ROW_PER_PAGE));

		addAllAttributeFromRequest(model, webRequest);
	}
}
