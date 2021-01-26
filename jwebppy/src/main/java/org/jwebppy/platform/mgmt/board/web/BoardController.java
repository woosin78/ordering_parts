package org.jwebppy.platform.mgmt.board.web;

import org.jwebppy.platform.mgmt.MgmtGeneralController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/platform/mgmt/board")
public class BoardController extends MgmtGeneralController
{
	@RequestMapping("/main")
	public String main()
	{
		return DEFAULT_VIEW_URL;
	}
}
