package org.jwebppy.platform.mgmt.common.web;

import java.util.HashMap;
import java.util.Map;

import org.jwebppy.platform.common.web.PlatformGeneralController;
import org.jwebppy.platform.mgmt.common.MgmtCommonVo;
import org.jwebppy.platform.core.util.CmStringUtils;
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

		model.addAttribute("pageNumber", CmStringUtils.defaultString(webRequest.getParameter("pageNumber"), "1"));
		model.addAttribute("rowPerPage", CmStringUtils.defaultString(webRequest.getParameter("rowPerPage"), MgmtCommonVo.DEFAULT_ROW_PER_PAGE));

		//HTML 페이지 javascript 에서 사용하는 상수 정의
		Map<String, String> constVariableMap = new HashMap<>();
		constVariableMap.put("USERNAME", getUsername());
		constVariableMap.put("NAME", UserAuthenticationUtils.getUserDetails().getName());
		constVariableMap.put("DATE_TIME_FORMAT", UserAuthenticationUtils.getUserDetails().getDateTimeFormat2());
		constVariableMap.put("DATE_FORMAT", UserAuthenticationUtils.getUserDetails().getDateFormat2());
		constVariableMap.put("TIME_FORMAT", UserAuthenticationUtils.getUserDetails().getTimeFormat2());
		constVariableMap.put("DELIMITER", PortalConfigVo.DELIMITER);
		constVariableMap.put("ROW_PER_PAGE", Integer.toString(MgmtCommonVo.DEFAULT_ROW_PER_PAGE));
		constVariableMap.put("YES", MgmtCommonVo.YES);
		constVariableMap.put("NO", MgmtCommonVo.NO);

		model.addAttribute("globalConstVariables", constVariableMap);
	}
}
