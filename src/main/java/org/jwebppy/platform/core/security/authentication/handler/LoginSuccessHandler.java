package org.jwebppy.platform.core.security.authentication.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.security.authentication.service.LoginHistoryService;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler
{
	@Autowired
	private LoginHistoryService loginHistoryService;

	private RequestCache requestCache = new HttpSessionRequestCache();

	public LoginSuccessHandler()
	{
	    super();
	}

	public LoginSuccessHandler(String defaultTargetUrl)
	{
		if (CmStringUtils.isNotEmpty(defaultTargetUrl))
		{
			setDefaultTargetUrl(defaultTargetUrl);
		}
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException
	{
		loginHistoryService.success(request, response);

		//Login 시 Save Username 체크 했을 경우 처리
        if (CmStringUtils.equals(request.getParameter("saveUsername"), PlatformCommonVo.YES))
        {
        	Cookie cookie = new Cookie(PlatformConfigVo.FORM_LOGIN_USERNAME, authentication.getName());
        	response.addCookie(cookie);
        	cookie.setMaxAge(10*365*24*60*60);
        }

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        //error 페이지로는 다시 이동시키지 않는다
        if (ObjectUtils.isEmpty(savedRequest) || CmStringUtils.endsWith(savedRequest.getRedirectUrl(), PlatformConfigVo.ERROR_PAGE_URL))
        {
        	getRedirectStrategy().sendRedirect(request, response, determineTargetUrl(request, response, authentication));
        }
        else
        {
        	super.onAuthenticationSuccess(request, response, authentication);
        }
	}
}
