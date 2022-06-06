package org.jwebppy.platform.core.security.authentication.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.security.authentication.AuthenticationType;
import org.jwebppy.platform.core.security.authentication.dto.LoginHistoryDto;
import org.jwebppy.platform.core.security.authentication.dto.LoginHistorySearchDto;
import org.jwebppy.platform.core.security.authentication.dto.PlatformUserDetails;
import org.jwebppy.platform.core.security.authentication.entity.LoginHistoryEntity;
import org.jwebppy.platform.core.security.authentication.mapper.LoginHistoryMapper;
import org.jwebppy.platform.core.security.authentication.mapper.LoginHistoryObjectMapper;
import org.jwebppy.platform.core.service.GeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoginHistoryService extends GeneralService
{
	@Autowired
	private LoginHistoryMapper loginHistoryMapper;

	@Autowired
	private RequestCache requestCache;

	@Autowired
	private UserService userService;

	public int success(HttpServletRequest request, HttpServletResponse response)
	{
		return createLoginHistory(request, response, PlatformCommonVo.YES);
	}

	public int fail(HttpServletRequest request, HttpServletResponse response)
	{
		return createLoginHistory(request, response, PlatformCommonVo.NO);
	}

	public int createLoginHistory(HttpServletRequest request, HttpServletResponse response, String fgResult)
	{
		String username = CmStringUtils.trimToEmpty(request.getParameter(PlatformConfigVo.FORM_LOGIN_USERNAME));

		if (CmStringUtils.isNotEmpty(username))
		{
			SavedRequest savedRequest = requestCache.getRequest(request, response);

			String referer = (savedRequest == null) ? request.getHeader("Referer") : savedRequest.getRedirectUrl();

			LoginHistoryEntity loginHistory = new LoginHistoryEntity();
			loginHistory.setUsername(username);
			loginHistory.setUserAgent(CmStringUtils.trimToEmpty(request.getHeader("user-agent")));
			loginHistory.setIp(getIp(request));
			loginHistory.setSessionId(request.getSession().getId());
			loginHistory.setReferer(referer);
			loginHistory.setFgResult(fgResult);
			loginHistory.setTimezone(PlatformCommonVo.DEFAULT_TIMEZONE);
			loginHistory.setAuthenticationType(AuthenticationType.N);

			if (UserAuthenticationUtils.isAuthenticated())
			{
				PlatformUserDetails platformUserDetails = UserAuthenticationUtils.getUserDetails();

				loginHistory.setUSeq(platformUserDetails.getUSeq());
				loginHistory.setTimezone(platformUserDetails.getTimezone());
				loginHistory.setAuthenticationType(platformUserDetails.getAuthenticationType());
			}

			return loginHistoryMapper.insertLoginHistory(loginHistory);
		}

		return 0;
	}

	public List<LoginHistoryDto> getPageableLoginHistories(LoginHistorySearchDto loginHistorySearch)
	{
		return CmModelMapperUtils.mapToDto(LoginHistoryObjectMapper.INSTANCE, loginHistoryMapper.findPageableLoginHistories(loginHistorySearch));
	}

	public int getLoginFailureCount(LoginHistorySearchDto loginHistorySearch)
	{
		return loginHistoryMapper.findLoginFailureCount(loginHistorySearch);
	}

	private String getIp(HttpServletRequest request)
	{
		String ip = CmStringUtils.trimToEmpty(request.getHeader("X-Forwarded-For"));

		if ("".equals(ip) || "unknown".equals(ip))
		{
			ip = CmStringUtils.trimToEmpty(request.getHeader("Proxy-Client-IP"));
		}
		if ("".equals(ip) || "unknown".equals(ip))
		{
			ip = CmStringUtils.trimToEmpty(request.getHeader("WL-Proxy-Client-IP")); // 웹로직
		}
		if ("".equals(ip) || "unknown".equals(ip))
		{
			ip = CmStringUtils.trimToEmpty(request.getHeader("HTTP_CLIENT_IP"));
		}
		if ("".equals(ip) || "unknown".equals(ip))
		{
			ip = CmStringUtils.trimToEmpty(request.getHeader("HTTP_X_FORWARDED_FOR"));
		}
		if ("".equals(ip) || "unknown".equals(ip))
		{
			ip = CmStringUtils.trimToEmpty(request.getRemoteAddr());
		}

		return ip;
	}
}
