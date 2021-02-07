package org.jwebppy.platform.mgmt.board.web;

import java.io.IOException;

import org.jwebppy.platform.mgmt.MgmtGeneralController;
import org.jwebppy.platform.mgmt.board.dto.BoardContentDto;
import org.jwebppy.platform.mgmt.board.service.BoardContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/platform/mgmt/board/content")
public class BoardContentController extends MgmtGeneralController
{
	@Autowired
	private BoardContentService boardContentService;

	@RequestMapping("/list")
	public String main()
	{
		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/write")
	public String write()
	{
		return DEFAULT_VIEW_URL;
	}

	@PostMapping("/save")
	@ResponseBody
	public Object save(@ModelAttribute("boardContent") BoardContentDto boardContent) throws IOException
	{
		return boardContentService.save(boardContent);
	}
}
