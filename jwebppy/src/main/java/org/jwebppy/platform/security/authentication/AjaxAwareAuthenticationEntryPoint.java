package org.jwebppy.platform.security.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jwebppy.platform.core.util.CmStringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

public class AjaxAwareAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint
{
	public AjaxAwareAuthenticationEntryPoint(String loginFormUrl)
	{
		super(loginFormUrl);
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException
	{
		if (isAjax(request))
		{
			response.sendError(403, "Forbidden");
		}
		else
		{
			super.commence(request, response, authenticationException);
		}
	}

	private boolean isAjax(HttpServletRequest request)
	{
		if (CmStringUtils.equals("XMLHttpRequest", request.getHeader("X-Requested-With")))
		{
			return true;
		}

		return false;
	}
}
