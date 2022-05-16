package org.jwebppy.portal.iv.uk.parts.domestic.common.web;

import org.jwebppy.portal.iv.uk.parts.common.web.UkPartsGeneralController;
import org.jwebppy.portal.iv.uk.parts.domestic.common.UkPartsDomesticCommonVo;
import org.springframework.ui.Model;
import org.springframework.web.context.request.WebRequest;

public abstract class UkPartsDomesticGeneralController extends UkPartsGeneralController
{
	protected final static String[] MANAGER_AUTHORITES = {"DP_IVDO_PARTS_MANAGER"};

	@Override
	protected void addAllAttributeFromRequest(Model model, WebRequest webRequest)
	{
		super.addAllAttributeFromRequest(model, webRequest);

		model.addAttribute("BASE_PATH", UkPartsDomesticCommonVo.REQUEST_PATH);
	}
}
