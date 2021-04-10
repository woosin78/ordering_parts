package org.jwebppy.portal.scm.parts.domestic;

import org.jwebppy.portal.scm.parts.PartsGeneralController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/portal/scm/domestic")
public class MainController extends PartsGeneralController
{
	@RequestMapping("/main")
	public String main(Model model)
	{
		return DEFAULT_VIEW_URL;
	}
}
