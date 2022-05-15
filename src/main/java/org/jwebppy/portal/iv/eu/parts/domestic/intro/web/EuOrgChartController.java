package org.jwebppy.portal.iv.eu.parts.domestic.intro.web;

import org.jwebppy.portal.iv.eu.parts.domestic.common.web.EuPartsDomesticGeneralController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping("/portal/corp/eu/scm/parts/intro")
public class EuOrgChartController extends EuPartsDomesticGeneralController {

	@RequestMapping("/org_chart")
	public String aboutUs(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);
		return DEFAULT_VIEW_URL;
	}
}
