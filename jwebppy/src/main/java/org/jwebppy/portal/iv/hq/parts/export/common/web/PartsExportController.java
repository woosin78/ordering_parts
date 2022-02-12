package org.jwebppy.portal.iv.hq.parts.export.common.web;

import org.jwebppy.portal.iv.hq.parts.export.common.PartsExportCommonVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PartsExportCommonVo.REQUEST_PATH)
public class PartsExportController extends PartsExportGeneralController
{
	@RequestMapping("/main")
	public String main(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}

	@RequestMapping("/preload")
	public String preload(Model model, WebRequest webRequest)
	{
		addAllAttributeFromRequest(model, webRequest);

		return DEFAULT_VIEW_URL;
	}
}
