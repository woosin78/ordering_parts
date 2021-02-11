package org.jwebppy.platform.mgmt.board.web;

import java.io.IOException;

import org.jwebppy.platform.core.web.ui.pagination.PageableList;
import org.jwebppy.platform.mgmt.MgmtGeneralController;
import org.jwebppy.platform.mgmt.board.dto.BoardContentDto;
import org.jwebppy.platform.mgmt.board.dto.BoardContentSearchDto;
import org.jwebppy.platform.mgmt.board.dto.BoardDto;
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
@RequestMapping("/platform/mgmt/board/content")
public class BoardContentController extends MgmtGeneralController
{
	@Autowired
	private BoardService boardService;

	@Autowired
	private BoardContentService boardContentService;

	@RequestMapping("/list")
	public String list(Model model, WebRequest webRequest)
	{
		BoardDto board = boardService.findBoard(webRequest.getParameter("bId"));

		model.addAttribute("board", board);

		addAllAttributeFromRequest(model, webRequest);

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
		BoardDto board = boardService.findBoard(webRequest.getParameter("bId"));

		model.addAttribute("board", board);

		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/write")
	public String write(@ModelAttribute("boardContentSearch") BoardContentSearchDto boardContentSearch)
	{
		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/detail/data")
	@ResponseBody
	public Object detailData(@RequestParam("bcSeq") Integer bcSeq)
	{
		return boardContentService.getBoardContent(bcSeq);
	}

	@PostMapping("/save")
	@ResponseBody
	public Object save(@ModelAttribute BoardContentDto boardContent) throws IOException
	{
		return boardContentService.save(boardContent);
	}

	@PostMapping("/delete")
	@ResponseBody
	public Object delete(@RequestParam("bcSeq") Integer bcSeq)
	{
		return boardContentService.delete(bcSeq);
	}
}
