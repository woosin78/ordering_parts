package org.jwebppy.platform.security.authentication.handler;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.mgmt.content.dto.CItemDto;
import org.jwebppy.platform.mgmt.content.dto.CItemSearchDto;
import org.jwebppy.platform.mgmt.content.service.ContentService;
import org.jwebppy.platform.mgmt.user.service.UserService;
import org.jwebppy.platform.security.authentication.dto.LoginHistorySearchDto;
import org.jwebppy.platform.security.authentication.service.LoginHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class LoginFailureHandler implements AuthenticationFailureHandler
{
	@Autowired
	private ContentService contentService;

	@Autowired
	private LoginHistoryService loginHistoryService;

	@Autowired
	private UserService userService;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException
	{
		String fgAccountLocked = checkAccountLockedByWrongPassword(request.getParameter(PlatformConfigVo.FORM_LOGIN_USERNAME), exception);

		loginHistoryService.createLoginHistory(request, PlatformCommonVo.NO, fgAccountLocked);

		if (exception instanceof LockedException || PlatformCommonVo.YES.equals(fgAccountLocked))
		{
			response.setStatus(HttpStatus.LOCKED.value());
		}
		else
		{
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
		}

		request.getRequestDispatcher(PlatformConfigVo.FORM_LOGIN_PAGE_URL).forward(request, response);
	}

	//Super Admin 의 경우 로그인 실패 횟수 체크
	protected String checkAccountLockedByWrongPassword(String username, AuthenticationException exception)
	{
		if (exception instanceof BadCredentialsException)
		{
			CItemSearchDto cItemSearch = new CItemSearchDto();
			cItemSearch.setUsername(username);
			cItemSearch.setType(PlatformCommonVo.ROLE);
			cItemSearch.setName(PlatformConfigVo.ROLE_PLTF_ADMIN);

			List<CItemDto> cItems = contentService.getMyItems(cItemSearch);

			if (CollectionUtils.isNotEmpty(cItems))
			{
				LoginHistorySearchDto loginHistorySearch = new LoginHistorySearchDto();
				loginHistorySearch.setUsername(username);

				if (loginHistoryService.getLoginFailureCount(loginHistorySearch) >= PlatformConfigVo.FORM_LOGIN_PASSWORD_FAIL_LIMIT_COUNT - 1)
				{
					userService.lockUserAccount(userService.getUserByUsername(username).getUSeq(), PlatformCommonVo.YES);

					return PlatformCommonVo.YES;
				}
			}
		}

		return PlatformCommonVo.NO;
	}
}

