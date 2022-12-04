package org.jwebppy.platform.core.security.authentication.handler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jwebppy.platform.core.PlatformCommonVo;
import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.security.authentication.AccountLockedReason;
import org.jwebppy.platform.core.security.authentication.dto.LoginHistoryDto;
import org.jwebppy.platform.core.security.authentication.dto.LoginHistorySearchDto;
import org.jwebppy.platform.core.security.authentication.service.LoginHistoryService;
import org.jwebppy.platform.core.util.CmClassUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.user.dto.CredentialsPolicyDto;
import org.jwebppy.platform.mgmt.user.dto.UserAccountDto;
import org.jwebppy.platform.mgmt.user.dto.UserDto;
import org.jwebppy.platform.mgmt.user.service.UserService;
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

	@Autowired
	private UserService userService;

	private String loginPageUrl = PlatformConfigVo.FORM_LOGIN_PAGE_URL;

	public LoginFailureHandler() {}

	public LoginFailureHandler(String loginPageUrl)
	{
		this.loginPageUrl = loginPageUrl;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException
	{
		loginHistoryService.fail(request, response);

		String username = CmStringUtils.trimToEmpty(request.getParameter(PlatformConfigVo.FORM_LOGIN_USERNAME));
		long[] checkResult = checkExceedAllowablePwdFailureCount(username);

		if (checkResult[0] > -1 && checkResult[0] >= checkResult[1])
		{
			UserAccountDto userAccount = userService.getUserByUsername(username).getUserAccount();

			request.getSession().setAttribute("ACCOUNT_LOCKED_REASON", AccountLockedReason.builder()
					.loginFreezingBy(LocalDateTime.now().plusSeconds(checkResult[2]))
					.credentialsPolicy(userAccount.getCredentialsPolicy())
					.build());
		}

		response.setHeader("Login-Error-Type", CmClassUtils.getShortClassName(exception.getClass()).replaceAll("Exception", "") + ":" + exception.getMessage());
		response.setStatus(getStatus(exception).value());

		request.setAttribute(PlatformConfigVo.FORM_LOGIN_FAIL_TYPE, CmClassUtils.getShortClassName(exception.getClass()).replaceAll("Exception", ""));
		request.setAttribute(PlatformConfigVo.FORM_LOGIN_FAIL_MESSAGE, exception.getMessage());

		//비밀번호가 만료되었을 경우 비밀번호 변경을 위해 username 을 세션에 저장하고 비밀번호 변경 페이지로 이동한다
		if (exception instanceof CredentialsExpiredException)
		{
			request.getSession().setAttribute(PlatformConfigVo.FORM_LOGIN_USERNAME, username);
		}

		request.getRequestDispatcher(loginPageUrl).forward(request, response);
	}

	public long[] checkExceedAllowablePwdFailureCount(String username)
	{
		UserDto user = userService.getUserByUsername(username);

		if (ObjectUtils.isNotEmpty(user))
		{
			CredentialsPolicyDto credentialsPolicy = user.getUserAccount().getCredentialsPolicy();

			if (CmStringUtils.equals(PlatformCommonVo.YES, credentialsPolicy.getFgUsePwdFailPenalty()))
			{
				long pwdFailCheckDuration = NumberUtils.toInt(credentialsPolicy.getPfailCheckDuration(), 0);

				if (pwdFailCheckDuration > 0)
				{
					int allowablePwdFailCount = NumberUtils.toInt(credentialsPolicy.getPallowableFailCount(), 0);

					if (allowablePwdFailCount > 0)
					{
						List<LoginHistoryDto> loginHistories = ListUtils.emptyIfNull(loginHistoryService.getPageableLoginHistories(LoginHistorySearchDto.builder()
								.username(username)
								.fromDate(LocalDateTime.now().minusSeconds(pwdFailCheckDuration))
								.build()));

						int failCount = 0;

						for (LoginHistoryDto loginHistory: loginHistories)
						{
							if (CmStringUtils.equals(PlatformCommonVo.YES, loginHistory.getFgResult()))
							{
								break;
							}

							failCount++;
						}

						return new long[] {failCount, allowablePwdFailCount, NumberUtils.toInt(credentialsPolicy.getPfreezingDuration(), 300)};
					}
				}
			}
		}

		return new long[] {-1, -1, -1};
	}

	protected HttpStatus getStatus(AuthenticationException exception)
	{
		HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

		if (exception instanceof LockedException || exception instanceof CredentialsExpiredException)
		{
			httpStatus = HttpStatus.LOCKED;
		}

		return httpStatus;
	}
}

