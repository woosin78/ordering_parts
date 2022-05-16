package org.jwebppy.portal.iv.uk.common.web;

import org.jwebppy.portal.iv.common.web.IvGeneralController;
import org.jwebppy.portal.iv.uk.common.UkCommonVo;
import org.springframework.ui.Model;
import org.springframework.web.context.request.WebRequest;

public class UkGeneralController extends IvGeneralController
{
	@Override
	protected void addAllAttributeFromRequest(Model model, WebRequest webRequest)
	{
		super.addAllAttributeFromRequest(model, webRequest);

		model.addAttribute("BASE_PATH", UkCommonVo.REQUEST_PATH);
	}
}
