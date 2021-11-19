package org.jwebppy.platform.core.security.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jwebppy.platform.core.PlatformConfigVo;
import org.jwebppy.platform.core.util.CmStringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class PlatformAuthenticationFilter extends UsernamePasswordAuthenticationFilter
{
	public PlatformAuthenticationFilter() {}

	public PlatformAuthenticationFilter(AuthenticationManager authenticationManager)
	{
		super.setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException
	{
		String username = CmStringUtils.trimToEmpty(request.getParameter(PlatformConfigVo.FORM_LOGIN_USERNAME));
		String password = CmStringUtils.trimToEmpty(request.getParameter(PlatformConfigVo.FORM_LOGIN_PASSWORD));

		if ("".equals(username))
		{
			throw new UsernameNotFoundException("The username or password is incorrect.");
		}

		if ("".equals(password))
		{
			throw new BadCredentialsException("The username or password is incorrect.");
		}

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);

		setDetails(request, usernamePasswordAuthenticationToken);

		return this.getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
	}
}
