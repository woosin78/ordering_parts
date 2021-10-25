package org.jwebppy.platform.core.security.authentication.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.security.authentication.service.LoginHistoryService;
import org.jwebppy.platform.core.util.CmClassUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class LoginFailureHandler implements AuthenticationFailureHandler
{
	@Autowired
	private LoginHistoryService loginHistoryService;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException
	{
		loginHistoryService.fail(request, response);

		response.setHeader("Login-Error-Type", CmClassUtils.getShortClassName(exception.getClass()).replaceAll("Exception", "") + ":" + exception.getMessage());
		response.setStatus(getStatus(exception).value());

		request.setAttribute(PlatformConfigVo.FORM_LOGIN_FAIL_TYPE, CmClassUtils.getShortClassName(exception.getClass()).replaceAll("Exception", ""));

		//비밀번호가 만료되었을 경우 비밀번호 변경을 위해 username 을 세션에 저장하고 비밀번호 변경 페이지로 이동한다
		if (exception instanceof CredentialsExpiredException)
		{
			request.getSession().setAttribute(PlatformConfigVo.FORM_LOGIN_USERNAME, request.getParameter(PlatformConfigVo.FORM_LOGIN_USERNAME));
		}

		request.getRequestDispatcher(PlatformConfigVo.FORM_LOGIN_PAGE_URL).forward(request, response);
	}

	private HttpStatus getStatus(AuthenticationException exception)
	{
		HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

		if (exception instanceof LockedException || exception instanceof CredentialsExpiredException)
		{
			httpStatus = HttpStatus.LOCKED;
		}

		return httpStatus;
	}
}

