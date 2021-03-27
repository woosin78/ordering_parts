package org.jwebppy.portal.dbkr.scm.parts.export;

import org.jwebppy.portal.dbkr.scm.ScmGeneralController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/portal/scm/export")
public class MainController extends ScmGeneralController
{
	@RequestMapping("/main")
	public String main(Model model)
	{
		return DEFAULT_VIEW_URL;
	}
}
