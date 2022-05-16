package org.jwebppy.portal.iv.eu.parts.domestic.intro.web;

import org.jwebppy.portal.iv.eu.parts.domestic.common.EuPartsDomesticCommonVo;
import org.jwebppy.portal.iv.eu.parts.domestic.common.web.EuPartsDomesticGeneralController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(EuPartsDomesticCommonVo.REQUEST_PATH  + "/intro")
public class EuAboutUsController extends EuPartsDomesticGeneralController
{
	@RequestMapping("/about_us")
	public String aboutUs(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);
		return DEFAULT_VIEW_URL;
	}
}