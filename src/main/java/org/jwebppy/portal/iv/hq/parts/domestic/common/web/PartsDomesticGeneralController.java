package org.jwebppy.portal.iv.hq.parts.domestic.common.web;

import org.jwebppy.portal.iv.hq.parts.common.web.PartsGeneralController;
import org.jwebppy.portal.iv.hq.parts.domestic.common.PartsDomesticCommonVo;
import org.springframework.ui.Model;
import org.springframework.web.context.request.WebRequest;

public class PartsDomesticGeneralController extends PartsGeneralController
{
	protected final String[] MANAGER_AUTHORITIES = {"DP_IVDO_PARTS_MANAGER"};

	@Override
	protected void addAllAttributeFromRequest(Model model, WebRequest webRequest)
	{
		super.addAllAttributeFromRequest(model, webRequest);

		model.addAttribute("BASE_PATH", PartsDomesticCommonVo.REQUEST_PATH);
	}

	protected boolean isManager()
	{
		return hasAuthority(MANAGER_AUTHORITIES);
	}
}
