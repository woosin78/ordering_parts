package org.jwebppy.platform.core.security.authentication.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jwebppy.platform.core.security.authentication.service.LoginHistoryService;
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

		super.onAuthenticationSuccess(request, response, authentication);
	}
}
