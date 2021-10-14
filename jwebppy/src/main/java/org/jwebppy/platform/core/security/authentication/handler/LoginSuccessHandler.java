package org.jwebppy.platform.core.security.authentication.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.security.authentication.service.LoginHistoryService;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler
{
	@Autowired
	private LoginHistoryService loginHistoryService;

	public LoginSuccessHandler()
	{
	    super();
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException
	{
		loginHistoryService.success(request, response);

		//Login 시 Save Username 체크 했을 경우 처리
        if (CmStringUtils.equals(request.getParameter("saveUsername"), PlatformCommonVo.YES))
        {
        	Cookie cookie = new Cookie("username", authentication.getName());
        	response.addCookie(cookie);
        }

		super.onAuthenticationSuccess(request, response, authentication);
	}
}
