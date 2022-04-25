package org.jwebppy.portal.iv.hq.common.web;

import org.jwebppy.portal.iv.common.web.IvGeneralController;
import org.jwebppy.portal.iv.hq.common.HqCommonVo;
import org.springframework.ui.Model;
import org.springframework.web.context.request.WebRequest;

public class HqGeneralController extends IvGeneralController
{
	protected final String[] MANAGER_AUTHORITIES = {"DP_IVDO_PARTS_MANAGER", "DP_IVEX_PARTS_MANAGER"};

	@Override
	protected void addAllAttributeFromRequest(Model model, WebRequest webRequest)
	{
		super.addAllAttributeFromRequest(model, webRequest);

		model.addAttribute("BASE_PATH", HqCommonVo.REQUEST_PATH);
	}
}
