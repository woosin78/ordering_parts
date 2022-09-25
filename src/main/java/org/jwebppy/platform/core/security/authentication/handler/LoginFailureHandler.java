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
import org.jwebppy.platform.core.security.authentication.dto.LoginHistoryDto;
import org.jwebppy.platform.core.security.authentication.dto.LoginHistorySearchDto;
import org.jwebppy.platform.core.security.authentication.service.LoginHistoryService;
import org.jwebppy.platform.core.util.CmClassUtils;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.jwebppy.platform.mgmt.user.dto.CredentialsPolicyDto;
import org.jwebppy.platform.mgmt.user.dto.UserDto;
import org.jwebppy.platform.mgmt.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class LoginFailureHandler implements AuthenticationFailureHandler
{
	private Logger logger = LoggerFactory.getLogger(LoginFailureHandler.class);

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
		long[] failResult = pwdFailCheck(username);

		System.err.println("100. Password Fail Penalty Check - Username:" + username + ", Fail/Allowable:" + failResult[0] + "/" + failResult[1]);

		logger.debug("100. Password Fail Penalty Check - Username:" + username + ", Fail/Allowable:" + failResult[0] + "/" + failResult[1]);

		if (failResult[0] > failResult[1])
		{
			request.getSession().setAttribute("PWD_PENALTY_TIME", LocalDateTime.now().plusSeconds(300));
		}

		response.setHeader("Login-Error-Type", CmClassUtils.getShortClassName(exception.getClass()).replaceAll("Exception", "") + ":" + exception.getMessage());
		response.setHeader("Pwd-Fail-Count", failResult[0] + PlatformConfigVo.DELIMITER + failResult[1]);
		response.setStatus(getStatus(exception).value());

		request.setAttribute(PlatformConfigVo.FORM_LOGIN_FAIL_TYPE, CmClassUtils.getShortClassName(exception.getClass()).replaceAll("Exception", ""));

		//비밀번호가 만료되었을 경우 비밀번호 변경을 위해 username 을 세션에 저장하고 비밀번호 변경 페이지로 이동한다
		if (exception instanceof CredentialsExpiredException)
		{
			request.getSession().setAttribute(PlatformConfigVo.FORM_LOGIN_USERNAME, username);
		}

		//request.getRequestDispatcher(PlatformConfigVo.FORM_LOGIN_PAGE_URL).forward(request, response);
		request.getRequestDispatcher(loginPageUrl).forward(request, response);
	}

	public long[] pwdFailCheck(String username)
	{
		UserDto user = userService.getUserByUsername(username);

		System.err.println("user:" + user);

		if (ObjectUtils.isNotEmpty(user))
		{
			CredentialsPolicyDto credentialsPolicy = user.getUserAccount().getCredentialsPolicy();

			System.err.println("credentialsPolicy:" + credentialsPolicy);

			if (CmStringUtils.equals(PlatformCommonVo.YES, credentialsPolicy.getFgUsePwdFailPenalty()))
			{
				long pwdFailCheckDuration = NumberUtils.toInt(credentialsPolicy.getPFailCheckDuration(), 0);

				System.err.println("pwdFailCheckDuration:" + pwdFailCheckDuration);

				if (pwdFailCheckDuration > 0)
				{
					int allowablePwdFailCount = NumberUtils.toInt(credentialsPolicy.getPAllowableFailCount(), 0);

					System.err.println("allowablePwdFailCount:" + allowablePwdFailCount);

					if (allowablePwdFailCount > 0)
					{
						LoginHistorySearchDto loginHistorySearch = new LoginHistorySearchDto();
						loginHistorySearch.setUsername(username);
						loginHistorySearch.setRegDate(LocalDateTime.now().minusSeconds(pwdFailCheckDuration));

						List<LoginHistoryDto> loginHistories = ListUtils.emptyIfNull(loginHistoryService.getPageableLoginHistories(loginHistorySearch));

						System.err.println("loginHistories.size():" + loginHistories.size());

						int failCount = 0;

						for (LoginHistoryDto loginHistory: loginHistories)
						{
							if (CmStringUtils.equals(PlatformCommonVo.YES, loginHistory.getFgResult()))
							{
								break;
							}

							failCount++;
						}

						return new long[] {failCount, allowablePwdFailCount, 300};
					}
				}
			}
		}

		return new long[] {0, 0, 0};
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

