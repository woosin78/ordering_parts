package org.jwebppy.platform.mgmt.common.web;

import org.jwebppy.platform.common.web.PlatformGeneralController;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.portal.common.PortalConfigVo;
import org.springframework.ui.Model;
import org.springframework.web.context.request.WebRequest;

import com.google.common.collect.ImmutableMap;

public class MgmtGeneralController extends PlatformGeneralController
{
	@Override
	protected void addAllAttributeFromRequest(Model model, WebRequest webRequest)
	{
		super.addAllAttributeFromRequest(model, webRequest);

		model.addAttribute("pageNumber", CmStringUtils.defaultString(webRequest.getParameter("pageNumber"), "1"));
		model.addAttribute("rowPerPage", CmStringUtils.defaultString(webRequest.getParameter("rowPerPage"), PlatformCommonVo.DEFAULT_ROW_PER_PAGE));

		//HTML 페이지 javascript 에서 사용하는 상수 정의
		ImmutableMap<String, String> constVariableMap = new ImmutableMap.Builder<String, String>()
				.put("USERNAME", getUsername())
				.put("NAME", UserAuthenticationUtils.getUserDetails().getName())
				.put("DATE_TIME_FORMAT", UserAuthenticationUtils.getUserDetails().getDateTimeFormat2())
				.put("DATE_FORMAT", UserAuthenticationUtils.getUserDetails().getDateFormat2())
				.put("TIME_FORMAT", UserAuthenticationUtils.getUserDetails().getTimeFormat2())
				.put("DELIMITER", PortalConfigVo.DELIMITER)
				.put("ROW_PER_PAGE", Integer.toString(PlatformCommonVo.DEFAULT_ROW_PER_PAGE))
				.put("YES", PlatformCommonVo.YES)
				.put("NO", PlatformCommonVo.NO)
				.build();

		model.addAttribute("globalConstVariables", constVariableMap);
	}
}
