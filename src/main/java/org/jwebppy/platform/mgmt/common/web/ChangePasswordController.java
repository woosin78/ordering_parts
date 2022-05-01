package org.jwebppy.platform.mgmt.common.web;

import javax.servlet.http.HttpSession;

import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.common.service.ChangePasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping(PlatformConfigVo.FORM_AUTHENTICATION_PATH)
public class ChangePasswordController extends MgmtGeneralController
{
	@Autowired
	private ChangePasswordService changePasswordService;

	@RequestMapping(PlatformConfigVo.FORM_PASSWORD_CHANGE_PAGE_PATH)
	public Object changePassword()
	{
		return DEFAULT_VIEW_URL;
	}

	@PostMapping(PlatformConfigVo.FORM_PASSWORD_CHANGE_PROCESSING_PATH)
	@ResponseBody
	public Object checkValidPassword(HttpSession httpSession, WebRequest webRequest)
	{
		String username = CmStringUtils.defaultIfEmpty(httpSession.getAttribute(PlatformConfigVo.FORM_LOGIN_USERNAME), UserAuthenticationUtils.getUsername());
		String password = CmStringUtils.trimToEmpty(webRequest.getParameter("password"));
		String newPassword = CmStringUtils.trimToEmpty(webRequest.getParameter("newPassword"));
		String confirmPassword = CmStringUtils.trimToEmpty(webRequest.getParameter("confirmPassword"));

		return changePasswordService.checkValidPassword(username, password, newPassword, confirmPassword);
	}
}
