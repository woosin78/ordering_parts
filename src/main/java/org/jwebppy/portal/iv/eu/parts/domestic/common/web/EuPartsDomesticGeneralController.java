package org.jwebppy.portal.iv.eu.parts.domestic.common.web;

import org.jwebppy.portal.iv.eu.parts.common.web.EuPartsGeneralController;
import org.jwebppy.portal.iv.eu.parts.domestic.common.EuPartsDomesticCommonVo;
import org.springframework.ui.Model;
import org.springframework.web.context.request.WebRequest;

public abstract class EuPartsDomesticGeneralController extends EuPartsGeneralController
{
	protected final static String[] MANAGER_AUTHORITES = {"DP_IVDO_PARTS_MANAGER"};

	@Override
	protected void addAllAttributeFromRequest(Model model, WebRequest webRequest)
	{
		super.addAllAttributeFromRequest(model, webRequest);

		model.addAttribute("BASE_PATH", EuPartsDomesticCommonVo.REQUEST_PATH);
	}
}
