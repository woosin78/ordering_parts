package org.jwebppy.platform.core.security.authentication.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.security.authentication.AuthenticationType;
import org.jwebppy.platform.core.security.authentication.dto.PlatformUserDetails;
import org.jwebppy.platform.core.security.authentication.service.LoginHistoryService;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.user.dto.UserAccountDto;
import org.jwebppy.platform.mgmt.user.dto.UserDto;
import org.jwebppy.platform.mgmt.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler
{
	private Logger logger = LoggerFactory.getLogger(LoginSuccessHandler.class);

	@Autowired
	private LoginHistoryService loginHistoryService;

	@Autowired
	private UserService userService;

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
		PlatformUserDetails platformUserDetails = UserAuthenticationUtils.getUserDetails();
		AuthenticationType authenticationType = platformUserDetails.getAuthenticationType();

		if (AuthenticationType.D.equals(authenticationType))
		{
			UserDto user = userService.getUser(UserAuthenticationUtils.getUseq());
			UserAccountDto userAccount = user.getUserAccount();

			logger.info("99. User Account Update");
			logger.info("99.1. Before:" + userAccount);

			userAccount.setFgAccountLocked(PlatformCommonVo.NO);
			userAccount.setFgPasswordLocked(PlatformCommonVo.NO);
			userAccount.setPassword(CmStringUtils.trimToEmpty(request.getParameter(PlatformConfigVo.FORM_LOGIN_PASSWORD)));

			if (!userAccount.isValidPeriod())
			{
    			userAccount.setFromValid(null);
    			userAccount.setToValid(null);
			}

			logger.info("99.2. After:" + userAccount);

			userService.saveUserAccount(userAccount);
		}
		else if (AuthenticationType.F.equals(authenticationType))
		{
			request.getSession().setAttribute("REAL_USERNAME", platformUserDetails.getRealUsername());
		}

		loginHistoryService.success(request, response);

		//???????????? ?????? ?????? ??????  ?????? ????????? ?????? ?????? ?????? ?????? ?????????
		request.getSession().removeAttribute("ACCOUNT_LOCKED_REASON");

		//Login ??? Save Username ?????? ?????? ?????? ??????
        if (CmStringUtils.equals(request.getParameter("saveUsername"), PlatformCommonVo.YES))
        {
        	Cookie cookie = new Cookie(PlatformConfigVo.FORM_LOGIN_USERNAME, authentication.getName());
        	response.addCookie(cookie);
        	cookie.setMaxAge(365*24*60*60);
        }

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        //error ??????????????? ?????? ??????????????? ?????????
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
