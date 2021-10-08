package org.jwebppy.platform.core.security.authentication.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.security.authentication.dto.LoginHistoryDto;
import org.jwebppy.platform.core.security.authentication.dto.LoginHistorySearchDto;
import org.jwebppy.platform.core.security.authentication.entity.LoginHistoryEntity;
import org.jwebppy.platform.core.security.authentication.mapper.LoginHistoryMapper;
import org.jwebppy.platform.core.security.authentication.mapper.LoginHistoryObjectMapper;
import org.jwebppy.platform.core.service.GeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.dto.CItemType;
import org.jwebppy.platform.mgmt.content.service.ContentService;
import org.jwebppy.platform.mgmt.user.dto.UserDto;
import org.jwebppy.platform.mgmt.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoginHistoryService extends GeneralService
{
	@Autowired
	private ContentService contentService;

	@Autowired
	private LoginHistoryMapper loginHistoryMapper;

	@Autowired
	private RequestCache requestCache;

	@Autowired
	private UserService userService;

	public int success(HttpServletRequest request, HttpServletResponse response)
	{
		return createLoginHistory(request, response, PlatformCommonVo.YES, null);
	}

	public int fail(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
	{
		String fgAccountLocked = checkAccountLockedByWrongPassword(request.getParameter(PlatformConfigVo.FORM_LOGIN_USERNAME), exception);

		return createLoginHistory(request, response, PlatformCommonVo.YES, fgAccountLocked);
	}

	public int createLoginHistory(HttpServletRequest request, HttpServletResponse response, String fgResult, String fgAccountLocked)
	{
		SavedRequest savedRequest = requestCache.getRequest(request, response);

		String referer = (savedRequest == null) ? request.getHeader("Referer") : savedRequest.getRedirectUrl();

		LoginHistoryEntity loginHistory = new LoginHistoryEntity();
		loginHistory.setUsername(request.getParameter(PlatformConfigVo.FORM_LOGIN_USERNAME));
		loginHistory.setUserAgent(CmStringUtils.trimToEmpty(request.getHeader("user-agent")));
		loginHistory.setIp(getIp(request));
		loginHistory.setSessionId(request.getSession().getId());
		loginHistory.setReferer(referer);
		loginHistory.setFgResult(fgResult);
		loginHistory.setTimezone(PlatformCommonVo.DEFAULT_TIMEZONE);

		if (CmStringUtils.equals(fgAccountLocked, PlatformCommonVo.YES))
		{
			loginHistory.setAccountLockedDate(LocalDateTime.now());
		}

		if (UserAuthenticationUtils.isLogin())
		{
			UserDto user = userService.getUser(UserAuthenticationUtils.getUserDetails().getUSeq());

			loginHistory.setUSeq(user.getUSeq());
			loginHistory.setTimezone(user.getUserContactInfo().getTimezone());
		}

		return loginHistoryMapper.insertLoginHistory(loginHistory);
	}

	public List<LoginHistoryDto> getPageableLoginHistories(LoginHistorySearchDto loginHistorySearch)
	{
		return CmModelMapperUtils.mapToDto(LoginHistoryObjectMapper.INSTANCE, loginHistoryMapper.findPageableLoginHistories(loginHistorySearch));
	}

	public int getLoginFailureCount(LoginHistorySearchDto loginHistorySearch)
	{
		return loginHistoryMapper.findLoginFailureCount(loginHistorySearch);
	}

	//Super Admin 의 경우 로그인 실패 횟수 체크
	private String checkAccountLockedByWrongPassword(String username, AuthenticationException exception)
	{
		if (exception instanceof BadCredentialsException)
		{
			CItemSearchDto cItemSearch = new CItemSearchDto();
			cItemSearch.setUsername(username);
			cItemSearch.setType(CItemType.R);
			cItemSearch.setName(PlatformConfigVo.ROLE_PLTF_ADMIN);

			List<CItemDto> cItems = contentService.getMyItems(cItemSearch);

			if (CollectionUtils.isNotEmpty(cItems))
			{
				LoginHistorySearchDto loginHistorySearch = new LoginHistorySearchDto();
				loginHistorySearch.setUsername(username);

				if (getLoginFailureCount(loginHistorySearch) >= PlatformConfigVo.FORM_LOGIN_PASSWORD_FAIL_LIMIT_COUNT - 1)
				{
					userService.lockUserAccount(userService.getUserByUsername(username).getUSeq(), PlatformCommonVo.YES);

					return PlatformCommonVo.YES;
				}
			}
		}

		return PlatformCommonVo.NO;
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
