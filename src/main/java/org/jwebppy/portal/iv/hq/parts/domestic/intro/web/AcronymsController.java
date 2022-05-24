package org.jwebppy.portal.iv.hq.parts.domestic.intro.web;

import org.jwebppy.portal.iv.hq.parts.domestic.common.PartsDomesticCommonVo;
import org.jwebppy.portal.iv.hq.parts.domestic.common.web.PartsDomesticGeneralController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PartsDomesticCommonVo.REQUEST_PATH + "/intro/acronyms")
public class AcronymsController extends PartsDomesticGeneralController
{
	@RequestMapping("/list")
	public String list(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}
}
