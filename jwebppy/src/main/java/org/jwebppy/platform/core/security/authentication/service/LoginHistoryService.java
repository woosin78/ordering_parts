package org.jwebppy.platform.core.security.authentication.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.security.authentication.dto.LoginHistoryDto;
import org.jwebppy.platform.core.security.authentication.dto.LoginHistorySearchDto;
import org.jwebppy.platform.core.security.authentication.entity.LoginHistoryEntity;
import org.jwebppy.platform.core.security.authentication.mapper.LoginHistoryMapper;
import org.jwebppy.platform.core.service.GeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoginHistoryService extends GeneralService
{
	@Autowired
	private LoginHistoryMapper loginHistoryMapper;

	public int createLoginHistory(HttpServletRequest request, String fgLoginResult)
	{
		return createLoginHistory(request, fgLoginResult, null);
	}

	public int createLoginHistory(HttpServletRequest request, String fgLoginResult, String fgAccountLocked)
	{
		LoginHistoryEntity loginHistory = new LoginHistoryEntity();
		loginHistory.setUsername(request.getParameter(PlatformConfigVo.FORM_LOGIN_USERNAME));
		loginHistory.setUserAgent(CmStringUtils.trimToEmpty(request.getHeader("user-agent")));
		loginHistory.setIp(getIp(request));
		loginHistory.setSessionId(request.getSession().getId());
		loginHistory.setReferer(CmStringUtils.trimToEmpty(request.getHeader("referer")));
		loginHistory.setFgResult(fgLoginResult);
		loginHistory.setFgAccountLocked(fgAccountLocked);

		if (CmStringUtils.equals(fgAccountLocked, PlatformCommonVo.YES))
		{
			loginHistory.setAccountLockedDate(LocalDateTime.now());
		}

		if (UserAuthenticationUtils.getUserDetails() != null)
		{
			loginHistory.setUSeq(UserAuthenticationUtils.getUserDetails().getUSeq());
		}

		return loginHistoryMapper.insertLoginHistory(loginHistory);
	}

	public List<LoginHistoryDto> getPageableLoginHistories(LoginHistorySearchDto loginHistorySearch)
	{
		return CmModelMapperUtils.mapAll(loginHistoryMapper.findPageableLoginHistories(loginHistorySearch), LoginHistoryDto.class);
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
