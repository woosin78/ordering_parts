package org.jwebppy.platform.mgmt.board.web;

import org.jwebppy.platform.mgmt.MgmtGeneralController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/platform/mgmt/board/list")
public class BoardListController extends MgmtGeneralController
{
	@RequestMapping("/main")
	public String main()
	{
		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/write")
	public String write()
	{
		return DEFAULT_VIEW_URL;
	}
}
