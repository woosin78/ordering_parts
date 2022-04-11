package org.jwebppy.portal.iv.eu.common.web;

import org.jwebppy.portal.iv.common.web.IvGeneralController;
import org.jwebppy.portal.iv.eu.common.EuCommonVo;
import org.springframework.ui.Model;
import org.springframework.web.context.request.WebRequest;

public class EuGeneralController extends IvGeneralController
{
	@Override
	protected void addAllAttributeFromRequest(Model model, WebRequest webRequest)
	{
		super.addAllAttributeFromRequest(model, webRequest);

		model.addAttribute("BASE_PATH", EuCommonVo.REQUEST_PATH);
	}
}
