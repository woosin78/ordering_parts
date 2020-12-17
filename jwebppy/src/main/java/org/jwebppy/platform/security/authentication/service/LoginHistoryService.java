package org.jwebppy.platform.security.authentication.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.service.GeneralService;
import org.jwebppy.platform.core.util.CmModelMapperUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.core.util.UserAuthenticationUtils;
import org.jwebppy.platform.security.authentication.dto.LoginHistoryDto;
import org.jwebppy.platform.security.authentication.dto.LoginHistorySearchDto;
import org.jwebppy.platform.security.authentication.entity.LoginHistoryEntity;
import org.jwebppy.platform.security.authentication.mapper.LoginHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginHistoryService extends GeneralService
{
	@Autowired
	private LoginHistoryMapper loginHistoryMapper;

	public int createLoginHistory(HttpServletRequest request, String fgLoginResult)
	{
		return createLoginHistory(request, fgLoginResult, null);
	}

	@Transactional
	public int createLoginHistory(HttpServletRequest request, String fgLoginResult, String fgAccountLocked)
	{
		LoginHistoryEntity loginHistoryEntity = new LoginHistoryEntity();
		loginHistoryEntity.setUsername(request.getParameter(PlatformConfigVo.FORM_LOGON_USERNAME));
		loginHistoryEntity.setUserAgent(CmStringUtils.trimToEmpty(request.getHeader("user-agent")));
		loginHistoryEntity.setIp(getIp(request));
		loginHistoryEntity.setSessionId(request.getSession().getId());
		loginHistoryEntity.setReferer(CmStringUtils.trimToEmpty(request.getHeader("referer")));
		loginHistoryEntity.setFgResult(fgLoginResult);
		loginHistoryEntity.setFgAccountLocked(fgAccountLocked);

		if (CmStringUtils.equals(fgAccountLocked, PlatformCommonVo.YES))
		{
			loginHistoryEntity.setAccountLockedDate(LocalDateTime.now());
		}

		if (UserAuthenticationUtils.getUserDetails() != null)
		{
			loginHistoryEntity.setUSeq(UserAuthenticationUtils.getUserDetails().getUSeq());
		}

		return loginHistoryMapper.insertLoginHistory(loginHistoryEntity);
	}

	public List<LoginHistoryDto> getPageableLoginHistories(LoginHistorySearchDto loginHistorySearch)
	{
		return CmModelMapperUtils.mapAll(loginHistoryMapper.findLoginHistories(loginHistorySearch), LoginHistoryDto.class);
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
