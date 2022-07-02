package org.jwebppy.platform.mgmt.common.web;

import org.jwebppy.platform.common.web.PlatformGeneralController;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.portal.common.PortalConfigVo;
import org.springframework.ui.Model;
import org.springframework.web.context.request.WebRequest;

public class MgmtGeneralController extends PlatformGeneralController
{
	@Override
	protected void addAllAttributeFromRequest(Model model, WebRequest webRequest)
	{
		super.addAllAttributeFromRequest(model, webRequest);

		//html 에서 사용되는 상수
		model.addAttribute("USERNAME", getUsername());
		model.addAttribute("NAME", UserAuthenticationUtils.getUserDetails().getName());
		model.addAttribute("DATE_TIME_FORMAT", UserAuthenticationUtils.getUserDetails().getDateTimeFormat2());
		model.addAttribute("DATE_FORMAT", UserAuthenticationUtils.getUserDetails().getDateFormat2());
		model.addAttribute("TIME_FORMAT", UserAuthenticationUtils.getUserDetails().getTimeFormat2());
		model.addAttribute("DELIMITER", PortalConfigVo.DELIMITER);//split 구분자
		model.addAttribute("ROW_PER_PAGE", PlatformCommonVo.DEFAULT_ROW_PER_PAGE);
		model.addAttribute("YES", PlatformCommonVo.YES);
		model.addAttribute("NO", PlatformCommonVo.NO);
	}
}
