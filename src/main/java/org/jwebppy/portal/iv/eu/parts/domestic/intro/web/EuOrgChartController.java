package org.jwebppy.portal.iv.eu.parts.domestic.intro.web;

import org.jwebppy.portal.iv.eu.parts.common.web.EuPartsGeneralController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping("/portal/corp/eu/scm/parts/intro")
public class EuOrgChartController extends EuPartsGeneralController {

	@RequestMapping("/org_chart")
	public String aboutUs(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);
		return DEFAULT_VIEW_URL;
	}
}
