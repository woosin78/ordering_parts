package org.jwebppy.platform.core.util;

import org.jwebppy.platform.security.authentication.dto.PlatformUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserAuthenticationUtils
{
	private static final String ROLE_PREFIX = "ROLE_";

	public static Authentication getAuthentication()
	{
		return SecurityContextHolder.getContext().getAuthentication();
	}

	public static PlatformUserDetails getUserDetails()
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null)
		{
			return (PlatformUserDetails)SecurityContextHolder.getContext().getAuthentication().getDetails();
		}

		return null;
	}

	public static String getUsername()
	{
		try
		{
			return getAuthentication().getName();
		}
		catch (NullPointerException e)
		{
			return "";
		}
	}

	public static boolean isLogin()
	{
		String username = getUsername();

		if ("".equals(username) || "anonymousUser".equals(username))
		{
			return false;
		}

		return true;
	}

	public static boolean hasRole(String[] roles)
	{
		if (roles == null || roles.length == 0)
		{
			return false;
		}

		for (String role : roles)
		{
			if (hasRole(role))
			{
				return true;
			}
		}

		return false;
	}

	public static boolean hasRole(String role)
	{
		Authentication authentication = getAuthentication();

		if (authentication == null)
		{
			return false;
		}

		for (GrantedAuthority auth : authentication.getAuthorities())
		{
			if (CmStringUtils.equals(ROLE_PREFIX + role, auth.getAuthority()))
			{
				return true;
			}
		}

		return false;
	}
}
