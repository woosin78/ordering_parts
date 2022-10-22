package org.jwebppy.portal.iv.hq.parts.export.common.web;

import org.jwebppy.portal.iv.hq.parts.common.web.PartsGeneralController;
import org.jwebppy.portal.iv.hq.parts.export.common.PartsExportCommonVo;
import org.springframework.ui.Model;
import org.springframework.web.context.request.WebRequest;

public class PartsExportGeneralController extends PartsGeneralController
{
	protected final static String[] MANAGER_AUTHORITES = {"DP_IVEX_PARTS_MANAGER"};

	@Override
	protected void addAllAttributeFromRequest(Model model, WebRequest webRequest)
	{
		super.addAllAttributeFromRequest(model, webRequest);

		model.addAttribute("BASE_PATH", PartsExportCommonVo.REQUEST_PATH);
	}
}
